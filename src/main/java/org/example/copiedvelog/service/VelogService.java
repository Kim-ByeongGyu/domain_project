package org.example.copiedvelog.service;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.entity.Velog;
import org.example.copiedvelog.repository.VelogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VelogService {
    private final VelogRepository velogRepository;

    @Transactional
    public Velog saveVelog(Velog velog) {
        return velogRepository.save(velog);
    }

    public List<Velog> findAll() {
        return velogRepository.findAll();
    }
    public List<Velog> findByOwner(User owner) {
        return velogRepository.findByOwner(owner);
    }

    public Velog findByName(String name){
        return velogRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Velog findByOwnerAndName(User owner, String name) {
        return velogRepository.findByOwnerAndName(owner, name);
    }

    @Transactional
    public void delete(Velog velog) {
        velogRepository.delete(velog);
    }


}
