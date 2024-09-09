package practice.application.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 민감 정보들 가져오기 위한 {@code Configuration}.
 *
 * <p>{@code resources} 의 {@code private.properties} 내용 읽어서 가져옴.
 * <p>가져오는 내용들 :
 *
 * <li>{@code db_url}
 * <li>{@code db_username}
 * <li>{@code db_password}
 *
 * @see application.properties
 */
@Configuration
@PropertySource("classpath:private.properties")
public class PrivateConfig {}
