package com.fengwenyi.codegenerator.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.fengwenyi.codegenerator.Config;
import com.fengwenyi.codegenerator.bo.CodeGeneratorBo;
import com.fengwenyi.codegenerator.config.ErwinProperties;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author <a href="https://www.fengwenyi.com">Erwin Feng</a>
 * @since 2021-09-26
 */
@Component
public class MyAutoGenerator {

    private CodeGeneratorBo bo;
    private ErwinProperties erwinProperties;

    public MyAutoGenerator() {
    }

    public void execute() {
        FastAutoGenerator.create(dataSourceBuilder())
                .globalConfig(this::globalConfigBuilder)
                .packageConfig(this::packageConfigBuilder)
                .strategyConfig(this::strategyConfigBuilder)
                .execute();
    }

    public DataSourceConfig.Builder dataSourceBuilder() {
        return new DataSourceConfig.Builder(bo.getDbUrl(), bo.getUsername(), bo.getPassword());
    }

    public void globalConfigBuilder(GlobalConfig.Builder builder) {

        builder.fileOverride().author(bo.getAuthor());

        String outDir = Config.OUTPUT_DIR;
        if ("jar".equalsIgnoreCase(erwinProperties.getStartMethod()) && StringUtils.hasText(outDir)) {
            outDir = bo.getOutDir();
        }
        builder.outputDir(outDir);

        DateType dateType = DateType.TIME_PACK;
        if (!"8".equalsIgnoreCase(bo.getJdkVersion())) {
            dateType = DateType.ONLY_DATE;
        }
        builder.dateType(dateType);

        if (BooleanUtils.isTrue(bo.getSwaggerSupport())) {
            builder.enableSwagger();
        }

    }

    public void packageConfigBuilder(PackageConfig.Builder builder) {
        builder
                .parent(bo.getPackageName())
                // builder.moduleName("");
                .controller(bo.getPackageController())
                .entity(bo.getPackageEntity())
                .mapper(bo.getPackageMapper())
                .xml(bo.getPackageMapperXml())
                .service(bo.getPackageService())
                .serviceImpl(bo.getPackageServiceImpl());
    }

    public void strategyConfigBuilder(StrategyConfig.Builder builder) {
        builder.addInclude(bo.getTableNames())
                .addFieldPrefix(bo.getFieldPrefixes())
                .addTablePrefix(bo.getTablePrefixes())
                .addExclude(bo.getExcludeTableNames())
                .entityBuilder()
                .naming(NamingStrategy.underline_to_camel)
                //.enableChainModel()
                //.enableLombok()
                //.enableActiveRecord()
                .formatFileName(bo.getFileNamePatternEntity())
                .idType(IdType.ASSIGN_ID)
                .logicDeleteColumnName(bo.getFieldLogicDelete())
                .versionColumnName(bo.getFieldVersion())
                .superClass(bo.getSuperClassName())
                .addIgnoreColumns(bo.getIgnoreColumns())
                .mapperBuilder()
                .formatMapperFileName(bo.getFileNamePatternMapper())
                .formatXmlFileName(bo.getFileNamePatternMapperXml())
                .serviceBuilder()
                .formatServiceFileName(bo.getFileNamePatternService())
                .formatServiceImplFileName(bo.getFileNamePatternServiceImpl())
                .controllerBuilder()
                .enableRestStyle()
                .enableHyphenStyle();

        Entity.Builder entityBuilder = builder.entityBuilder();
        if (BooleanUtils.isTrue(bo.getLombokChainModel())) {
            entityBuilder.enableChainModel();
        }
        if (BooleanUtils.isTrue(bo.getLombokModel())) {
            entityBuilder.enableLombok();
        }
        if (BooleanUtils.isTrue(bo.getColumnConstant())) {
            entityBuilder.enableColumnConstant();
        }
        // 字段注解
        if (BooleanUtils.isTrue(bo.getFieldAnnotation())) {
            entityBuilder.enableTableFieldAnnotation();
        }

        Mapper.Builder mapperBuilder = builder.mapperBuilder();
        if (BooleanUtils.isTrue(bo.getBaseResultMap())) {
            mapperBuilder.enableBaseResultMap();
        }
        if (BooleanUtils.isTrue(bo.getBaseColumnList())) {
            mapperBuilder.enableBaseColumnList();
        }

        // 开启mapper注解
        if (BooleanUtils.isTrue(bo.getMapperAnnotation())) {
            mapperBuilder.enableMapperAnnotation();
        }
    }

    public void setBo(CodeGeneratorBo bo) {
        this.bo = bo;
    }

    @Autowired
    public void setErwinProperties(ErwinProperties erwinProperties) {
        this.erwinProperties = erwinProperties;
    }
}
