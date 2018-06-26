package com.imcode.imcms.domain.service.api;

import com.imcode.imcms.domain.dto.UserDTO;
import com.imcode.imcms.domain.exception.UserNotExistsException;
import com.imcode.imcms.domain.service.UserService;
import com.imcode.imcms.persistence.entity.User;
import com.imcode.imcms.persistence.repository.UserRepository;
import imcode.server.user.RoleId;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(int id) {
        return ofNullable(userRepository.findById(id))
                .orElseThrow(() -> new UserNotExistsException(id));
    }

    @Override
    public User getUser(String login) {
        return ofNullable(userRepository.findByLogin(login))
                .orElseThrow(() -> new UserNotExistsException(login));
    }

    @Override
    public List<UserDTO> getAdminUsers() {
        return userRepository.findUsersWithRoleIds(RoleId.USERADMIN_ID, RoleId.SUPERADMIN_ID)
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllActiveUsers() {
        return userRepository.findByActiveIsTrue()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    // TODO: 13.10.17 Was moved. Rewrite to human code.
    @Override
    public List<User> findAll(boolean includeExternal, boolean includeInactive) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> c = cb.createQuery(User.class);
        Root<User> user = c.from(User.class);

        Predicate criteria = cb.conjunction();

        if (!includeExternal) {
            criteria = cb.and(criteria, cb.notEqual(user.get("external"), 2));
        }

        if (!includeInactive) {
            criteria = cb.and(criteria, cb.isTrue(user.get("active")));
        }

        c.select(user).where(criteria);

        return entityManager.createQuery(c).getResultList();
    }

    // TODO: 13.10.17 Was moved. Rewrite to human code.
    @Override
    public List<User> findByNamePrefix(String prefix, boolean includeInactive) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> c = cb.createQuery(User.class);
        Root<User> user = c.from(User.class);

        Predicate criteria = cb.notEqual(user.get("external"), 2);

        if (!includeInactive) {
            criteria = cb.and(criteria, cb.isTrue(user.get("active")));
        }

        criteria = cb.and(
                criteria,
                cb.or(
                        cb.like(user.get("login"), prefix),
                        cb.like(user.get("email"), prefix),
                        cb.like(user.get("firstName"), prefix),
                        cb.like(user.get("lastName"), prefix),
                        cb.like(user.get("title"), prefix),
                        cb.like(user.get("company"), prefix)
                )
        );

        c.select(user).where(criteria).orderBy(cb.asc(user.get("lastName")), cb.asc(user.get("firstName")));

        return entityManager.createQuery(c).getResultList();
    }

}
