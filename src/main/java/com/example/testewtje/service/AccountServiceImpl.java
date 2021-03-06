package com.example.testewtje.service;

import com.example.testewtje.model.dto.Account;
import com.example.testewtje.model.entyties.AccountEntity;
import com.example.testewtje.model.entyties.ImageEntity;
import com.example.testewtje.model.mapper.AccountMapper;
import com.example.testewtje.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    @Override
    public List<AccountEntity> getAllAccounts() {
        return repository.findAll();
    }

    @Override
    public Page<AccountEntity> getAllAccounts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<AccountEntity> getAllAccountsByProperty(Account account, Pageable pageable) {
        List<AccountEntity> accounts = repository.findAll(Specification
                .where(hasName(account.getNickname()))
                .or(hasUsername(account.getUsername())));

        int page = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
        List<AccountEntity> paged = accounts
                .stream()
                .skip((long) pageable.getPageSize() * page)
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        return new PageImpl<>(paged, pageable, accounts.size());
    }

    @Override
    public void createAccount(Account account) {
        AccountEntity newAccount = mapper.map(account);
        Date currentDate = new Date();

        newAccount.setCreatedAt(currentDate);
        newAccount.setUpdatedAt(currentDate);
        repository.save(newAccount);

        newAccount.getImages().forEach(image -> {
            image.setOwnerId(newAccount.getId());
            image.setCreatedAt(currentDate);
            image.setUpdatedAt(currentDate);
        });
        repository.save(newAccount);
    }

    @Override
    public void deleteAccount(Long id) {
        repository.delete(repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Override
    public AccountEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public AccountEntity findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void saveImagesToAccount(List<ImageEntity> images, String username) {
        try {
            AccountEntity account = repository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            account.getImages().addAll(images);
            repository.save(account);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    static Specification<AccountEntity> hasName(String name) {
        return (image, cq, cb) -> cb.equal(image.get("name"), name);
    }

    static Specification<AccountEntity> hasUsername(String username) {
        return (image, cq, cb) -> cb.equal(image.get("username"), username);
    }

}
