package com.admin4j.job.anno;

import org.springframework.context.annotation.ComponentScan;

/**
 * @author andanyang
 * @since 2023/5/24 14:34
 */
@ComponentScan("com.admin4j.job")
//@MapperScan(basePackages = "com.admin4j.job.mapper")
public @interface EnableQuartzJob {
}
