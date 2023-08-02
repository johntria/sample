package com.logicea.cards.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.logicea.cards.exceptions.CardNotFoundException;
import com.logicea.cards.exceptions.InvalidCriteria;
import com.logicea.cards.exceptions.ResourcesNotPermitted;
import com.logicea.cards.models.Card;
import com.logicea.cards.models.Role;
import com.logicea.cards.models.Status;
import com.logicea.cards.repositories.CardRepository;
import com.logicea.cards.repositories.UserRepository;
import com.logicea.cards.requests.CreateCardDTO;
import com.logicea.cards.requests.SearchCardCriteriaDTO;
import com.logicea.cards.requests.UpdateCardDTO;
import com.logicea.cards.utils.EntityUtils;

import lombok.RequiredArgsConstructor;

/**
 * By using this service you can CRUD  & search for a {@link Card}
 */
@Service
@RequiredArgsConstructor
public class CardService {
	private final CardRepository cardRepository;
	private final UserRepository userRepository;

	/**
	 * Creates a new card with the provided information and associates it with the user identified by the given email.
	 *
	 * @param createCardDTO The DTO (Data Transfer Object) containing the information to create the card.
	 * @param email         The email of the user who is creating the card.
	 * @return The newly created card.
	 */
	public Card createCard(CreateCardDTO createCardDTO, String email) {
		var currentUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		var cardToBeStored = Card.builder()
				.name(createCardDTO.getName())
				.description(createCardDTO.getDescription())
				.color(createCardDTO.getColor())
				.status(Status.TODO)
				.creationDate(LocalDateTime.now())
				.user(currentUser)
				.build();
		return cardRepository.save(cardToBeStored);
	}

	/**
	 * Retrieves the card with the specified cardId, ensuring that the user identified by the given email has
	 * appropriate permissions to access the card.
	 *
	 * @param cardId The unique identifier of the card to retrieve.
	 * @param email  The email of the user who wants to access the card.
	 * @return The card with the provided cardId.
	 * @throws CardNotFoundException If the card with the given cardId does not exist.
	 * @throws ResourcesNotPermitted If the user does not have permission to access the card.
	 */
	public Card readCard(Integer cardId, String email) {
		areUserAllowedToAccessCard(cardId, email);
		return cardRepository.findById(cardId).get();
	}

	/**
	 * Updates the card with the information provided in the UpdateCardDTO, ensuring that the user identified by the
	 * given email has appropriate permissions to update the card.
	 *
	 * @param dto   The DTO containing the updated information for the card.
	 * @param email The email of the user who is updating the card.
	 * @return The updated card.
	 * @throws CardNotFoundException If the card with the given cardId does not exist.
	 * @throws ResourcesNotPermitted If the user does not have permission to update the card.
	 */
	public Card updateCard(UpdateCardDTO dto, String email) {
		Card card = readCard(dto.getCardId(), email);
		card.setName(dto.getName());
		card.setDescription(dto.getDescription());
		card.setColor(dto.getColor());
		card.setStatus(dto.getStatus());
		return cardRepository.save(card);
	}

	/**
	 * Deletes the card with the specified cardId, ensuring that the user identified by the given email has
	 * appropriate permissions to delete the card.
	 *
	 * @param cardId The unique identifier of the card to delete.
	 * @param email  The email of the user who wants to delete the card.
	 * @throws CardNotFoundException If the card with the given cardId does not exist.
	 * @throws ResourcesNotPermitted If the user does not have permission to delete the card.
	 */
	public void deleteCard(Integer cardId, String email) {
		areUserAllowedToAccessCard(cardId, email);
		cardRepository.deleteById(cardId);
	}

	/**
	 * Searches for cards based on the criteria specified in the SearchCardCriteriaDTO, ensuring that the user
	 * identified by the given email has appropriate permissions to access the search results.
	 *
	 * @param dto              The DTO containing the search criteria for cards.
	 * @param currentUserEmail The email of the user performing the search.
	 * @return A page of cards that match the specified search criteria.
	 * @throws InvalidCriteria           If the search criteria contain invalid fields or directions.
	 * @throws UsernameNotFoundException If the user with the given email is not found in the repository.
	 */
	public Page<Card> searchCards(SearchCardCriteriaDTO dto, String currentUserEmail) {
		Sort sortable = EntityUtils.validateAndGroupAllSorts(dto.getSortMap(), Card.class);
		Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), sortable);
		Integer currentUserId = null;
		if (!Role.ADMIN.equals(getCurrentRoleByUserEmail(currentUserEmail))) {
			currentUserId =
					userRepository.findUserIdByEmail(currentUserEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		}
		return cardRepository.searchCards(
				currentUserId,
				dto.getName(),
				dto.getColor(),
				Status.getValueByStatus(dto.getStatus()),
				dto.getCreationDate(),
				pageable);
	}

	/**
	 * Checks if the user with the given email is allowed to access the card with the specified cardId.
	 * If the user is an {@link Role#ADMIN}, they are granted access. Otherwise, it is checked whether the card
	 * belongs to the
	 * user.
	 *
	 * @param cardId The unique identifier of the card to be accessed.
	 * @param email  The email of the user attempting to access the card.
	 * @throws ResourcesNotPermitted If the user is not allowed to access the card with the specified cardId.
	 */
	private void areUserAllowedToAccessCard(Integer cardId, String email) {
		isCardExists(cardId);
		var currentUserRole = getCurrentRoleByUserEmail(email);
		if (Role.ADMIN.equals(currentUserRole)) return;
		if (cardRepository.isCardIdBelongToThisEmail(cardId, email)) return;
		throw new ResourcesNotPermitted(
				String.format(
						"You are not allowed to access card with id:%s", cardId
				)
		);
	}

	/**
	 * Checks if a card with the specified cardId exists in the card repository.
	 *
	 * @param cardId The unique identifier of the card to check.
	 * @throws CardNotFoundException If the card with the given cardId does not exist.
	 */
	private void isCardExists(Integer cardId) {
		if (!cardRepository.existsById(cardId))
			throw new CardNotFoundException("Card with given id not found");
	}

	/**
	 * Retrieves the role of the user identified by the given email from the user repository.
	 *
	 * @param email The email of the user to retrieve the role for.
	 * @return The Role of the user.
	 * @throws UsernameNotFoundException If the user with the given email is not found in the repository or if the
	 *                                   role is not found for the user.
	 */
	private Role getCurrentRoleByUserEmail(String email) {
		return userRepository.findRoleByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Role not found for the given user"));
	}

}