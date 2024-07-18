package com.groovegather.back.services;

import org.springframework.stereotype.Service;
import com.groovegather.back.entities.OperateEntity;
import com.groovegather.back.entities.OperateId;
import com.groovegather.back.repositories.OperateRepo;

@Service
public class OperateService {
    private final OperateRepo operateRepo;

    public OperateService(OperateRepo operateRepo) {
        this.operateRepo = operateRepo;
    }

    public void incrementLikes(String name, int likesToAdd) {
        // Supposons que 'name' représente le nom d'un projet ou d'une autre entité pertinente
        // Vous devez adapter cette logique selon votre modèle de données réel
        OperateEntity operateEntity = operateRepo.findByName(name);
        if (operateEntity != null) {
            operateEntity.setLikes(operateEntity.getLikes() + likesToAdd);
            operateRepo.save(operateEntity);
        }
    }
}
