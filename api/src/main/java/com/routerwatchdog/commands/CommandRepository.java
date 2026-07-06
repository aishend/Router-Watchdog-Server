package com.routerwatchdog.commands;

import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class CommandRepository {

    private final JpaCommandRepository jpaCommandRepository;

    public CommandRepository(JpaCommandRepository jpaCommandRepository) {
        this.jpaCommandRepository = jpaCommandRepository;
    }

    public PendingCommand save(PendingCommand command) {
        CommandEntity entity = toEntity(command);
        CommandEntity savedEntity = jpaCommandRepository.save(entity);

        return toDomain(savedEntity);
    }

    public PendingCommand findById(String commandId) {
        return jpaCommandRepository.findById(commandId)
                .map(this::toDomain)
                .orElse(null);
    }

    public PendingCommand update(PendingCommand command) {
        CommandEntity entity = toEntity(command);
        CommandEntity savedEntity = jpaCommandRepository.save(entity);

        return toDomain(savedEntity);
    }

    public Collection<PendingCommand> findAll() {
        return jpaCommandRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public Collection<PendingCommand> findAllByDeviceId(String deviceId) {
        return jpaCommandRepository.findAllByDeviceIdOrderByCreatedAtDesc(deviceId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private CommandEntity toEntity(PendingCommand command) {
        return new CommandEntity(
                command.id(),
                command.deviceId(),
                command.type(),
                command.status(),
                command.createdAt(),
                command.deliveredAt(),
                command.completedAt()
        );
    }

    private PendingCommand toDomain(CommandEntity entity) {
        return new PendingCommand(
                entity.getId(),
                entity.getDeviceId(),
                entity.getType(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getDeliveredAt(),
                entity.getCompletedAt()
        );
    }
}
