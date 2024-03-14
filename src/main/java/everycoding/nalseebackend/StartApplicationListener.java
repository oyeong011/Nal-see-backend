package everycoding.nalseebackend;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StartApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final JdbcTemplate jdbcTemplate;

    public StartApplicationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 여기에 SQL 스크립트를 실행하는 코드를 추가
        jdbcTemplate.execute("ALTER TABLE nalsee.users CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        // 추가적으로 필요한 SQL 명령을 여기에 실행할 수 있습니다.
    }
}
