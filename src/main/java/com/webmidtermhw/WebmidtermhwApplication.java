package com.webmidtermhw;

import com.webmidtermhw.dataAccessObjects.RepositoryForComment;
import com.webmidtermhw.dataAccessObjects.RepositoryForCommentReply;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = {RepositoryForCommentReply.class, RepositoryForComment.class})
@EnableJpaRepositories(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = RepositoryForCommentReply.class), @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = RepositoryForComment.class)})
@SpringBootApplication
public class WebmidtermhwApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebmidtermhwApplication.class, args);
    }

}
