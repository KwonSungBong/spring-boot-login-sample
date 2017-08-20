package com.example.demo.service;

import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service(value = "appUserService")
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;


    @Override
    @Transactional
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public long post(AppUser appUser) {
        return appUserRepository.save(appUser).getId();
    }

    @Transactional
    @Override
    public AppUser get(long id) {
        return appUserRepository.findOne(id);
    }

    @Transactional
    @Override
    public AppUser patch(AppUser appUser) {
        appUserRepository.save(appUser);
        return get(appUser.getId());
    }

    @Transactional
    @Override
    public boolean delete(long id) {
        appUserRepository.delete(id);
        return true;
    }
}
