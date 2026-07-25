package com.nexus.shop.api.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.model.store.dto.StoreDto;
import com.nexus.shop.model.store.entity.Store;
import com.nexus.shop.model.store.request.StoreCreateDTO;
import com.nexus.shop.persistence.repository.StoreRepository;
import com.nexus.shop.persistence.repository.UserRepository;
import com.nexus.shop.utils.converters.ConverterUtil;
import com.nexus.shop.utils.helpers.UserContextHelper;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public StoreService(final StoreRepository storeRepository, final UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    public StoreDto create(final StoreCreateDTO dto) {
        final Store store = new Store();
        store.setName(dto.name());
        store.setEmail(dto.email());
        store.setPhone(dto.phone());

        store.setOwner(userRepository.findByEmail(UserContextHelper.getCurrentUserEmail()).get());
        store.setTags(dto.tags().stream().map(tag -> {
            try {
                return Category.valueOf(tag.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category: " + tag);
            }
        }).toList());

        return ConverterUtil.toDTO(this.storeRepository.save(store));
    }

    public void delete(final UUID id) {
        final Store store = this.storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        if (!store.getOwner().getEmail().equals(UserContextHelper.getCurrentUserEmail())) {
            throw new IllegalArgumentException("You are not authorized to delete this store");
        }

        this.storeRepository.delete(store);
    }

    public StoreDto findById(final UUID id) {
        return ConverterUtil.toDTO(
                this.storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Store not found")));
    }

    public List<StoreDto> getAll() {
        return this.storeRepository.findAll().stream()
                .map(ConverterUtil::toDTO)
                .toList();
    }
}
