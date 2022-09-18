package com.springfield.framework;

import com.amazonaws.services.s3.AmazonS3;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.saasquatch.jsonschemainferrer.*;
import io.restassured.module.jsv.JsonSchemaValidatorSettings;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static com.springfield.framework.data.Constants.ALL_PROJECT_DIR_PATHS;
import static io.restassured.module.jsv.JsonSchemaValidator.settings;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpringfieldApplicationTests {

	protected String host;
	protected static JsonValidator validator;
	protected AmazonS3 s3client;

	protected static final JsonSchemaInferrer jsonSchemaInferrer = JsonSchemaInferrer.newBuilder()
			.setSpecVersion(SpecVersion.DRAFT_04)
			.addFormatInferrers(FormatInferrers.email(), FormatInferrers.ip())
			.setAdditionalPropertiesPolicy(AdditionalPropertiesPolicies.notAllowed())
			.setRequiredPolicy(RequiredPolicies.nonNullCommonFields())
			.addEnumExtractors(EnumExtractors.validEnum(java.time.Month.class), EnumExtractors.validEnum(java.time.DayOfWeek.class))
			.build();

	protected static final ValidationConfiguration validationConfig = ValidationConfiguration.newBuilder()
			.setDefaultVersion(DRAFTV4).freeze();

	protected static final JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder()
			.setValidationConfiguration(validationConfig).freeze();

	@BeforeAll
	public static void setupTest() {

		//s3client = getAmazonS3Client();

		settings = JsonSchemaValidatorSettings.settings()
				.with().jsonSchemaFactory(jsonSchemaFactory)
				.and().with().checkedValidation(true);

		validator = jsonSchemaFactory.getValidator();

		ALL_PROJECT_DIR_PATHS.forEach(filepath -> new File(filepath).mkdirs());
	}

	@AfterAll
	public static void tearTestSetup() {}




}
