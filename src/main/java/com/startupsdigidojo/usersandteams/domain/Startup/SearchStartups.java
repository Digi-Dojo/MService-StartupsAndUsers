package com.startupsdigidojo.usersandteams.domain.Startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SearchStartups {

    private StartupRepository startupRepository;

    @Autowired
    public SearchStartups(StartupRepository startupRepository) {
        this.startupRepository = startupRepository;
    }

    public Startup findOne(Long id){
        Optional<Startup> maybeStartup = startupRepository.findById(id);

        if(maybeStartup.isEmpty()){
            throw new IllegalArgumentException("Startup with id " + id + " is not present in the database");
        }

        return maybeStartup.get();
    }

    public List<Startup> findAll(){
        List<Startup> list = startupRepository.findAll();
        if(list.isEmpty()){
            throw new IllegalArgumentException("No startups in database");
        }
        return list;
    }
}