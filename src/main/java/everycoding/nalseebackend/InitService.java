package everycoding.nalseebackend;

import everycoding.nalseebackend.user.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final EntityManager em;

    public void dbInit() {

        User user = new User("John", "a@a.com");

        em.persist(user);
    }
}
