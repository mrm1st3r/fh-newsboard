package groovy.de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.dao.ExternModuleDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

/**
 * Created by Felix on 13.01.2017.
 */

@SpringBootTest(classes = NewsboardApplication.class)
class ExternModuleDaoTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    ExternModuleDao externModuleDao;


}
