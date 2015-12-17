package io.swagger.codegen.languages;

import io.swagger.codegen.CliOption;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.DefaultCodegen;
import io.swagger.codegen.SupportingFile;
import io.swagger.models.properties.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloFreshPhpClientCodegen extends DefaultCodegen implements CodegenConfig {
    static Logger LOGGER = LoggerFactory.getLogger(HelloFreshPhpClientCodegen.class);

    public static final String VARIABLE_NAMING_CONVENTION = "variableNamingConvention";
    public static final String PACKAGE_PATH = "packagePath";
    public static final String SRC_BASE_PATH = "srcBasePath";
    public static final String COMPOSER_VENDOR_NAME = "composerVendorName";
    public static final String COMPOSER_PROJECT_NAME = "composerProjectName";
    protected String invokerPackage = "HelloFresh\\HelloFreshClient";
    protected String composerVendorName = "swagger";
    protected String composerProjectName = "swagger-client";
    protected String packagePath = "SwaggerClient-php";
    protected String artifactVersion = "1.0.0";
    protected String srcBasePath = "lib";
    protected String variableNamingConvention= "snake_case";

    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    public String getName() {
        return "php";
    }

    public String getHelp() {
        return "Generates a PHP client library.";
    }

    public HelloFreshPhpClientCodegen() {
        super();

        reservedWords = new HashSet<String>(
                Arrays.asList(
                        "__halt_compiler", "abstract", "and", "array", "as", "break", "callable", "case", "catch", "class", "clone", "const", "continue", "declare", "default", "die", "do", "echo", "else", "elseif", "empty", "enddeclare", "endfor", "endforeach", "endif", "endswitch", "endwhile", "eval", "exit", "extends", "final", "for", "foreach", "function", "global", "goto", "if", "implements", "include", "include_once", "instanceof", "insteadof", "interface", "isset", "list", "namespace", "new", "or", "print", "private", "protected", "public", "require", "require_once", "return", "static", "switch", "throw", "trait", "try", "unset", "use", "var", "while", "xor")
        );

        // ref: http://php.net/manual/en/language.types.intro.php
        languageSpecificPrimitives = new HashSet<String>(
                Arrays.asList(
                        "bool",
                        "boolean",
                        "int",
                        "integer",
                        "double",
                        "float",
                        "string",
                        "object",
                        "DateTime",
                        "mixed",
                        "number",
                        "void",
                        "byte")
        );

        instantiationTypes.put("array", "array");
        instantiationTypes.put("map", "map");


        // provide primitives to mustache template
        String primitives = "'" + StringUtils.join(languageSpecificPrimitives, "', '") + "'";
        additionalProperties.put("primitives", primitives);

        // ref: https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#data-types
        typeMapping = new HashMap<String, String>();
        typeMapping.put("integer", "int");
        typeMapping.put("long", "int");
        typeMapping.put("float", "float");
        typeMapping.put("double", "double");
        typeMapping.put("string", "string");
        typeMapping.put("byte", "int");
        typeMapping.put("boolean", "bool");
        typeMapping.put("date", "\\DateTime");
        typeMapping.put("datetime", "\\DateTime");
        typeMapping.put("file", "\\SplFileObject");
        typeMapping.put("map", "map");
        typeMapping.put("array", "array");
        typeMapping.put("list", "array");
        typeMapping.put("object", "object");
        typeMapping.put("DateTime", "\\DateTime");

        outputFolder = "generated-code/hellofreshphp";
        modelTemplateFiles.put("model.mustache", ".php");
        apiTemplateFiles.put("api.mustache", ".php");
        embeddedTemplateDir = templateDir = "php";
        apiPackage = invokerPackage + "\\Api";
        modelPackage = invokerPackage + "\\Model";

        supportingFiles.add(new SupportingFile("HelloFreshClient.mustache", packagePath + "/lib", "HelloFreshClient.php"));
        supportingFiles.add(new SupportingFile("Deserializer.php", packagePath + "/lib", "Deserializer.php"));
        supportingFiles.add(new SupportingFile("AbstractModel.php", packagePath + "/lib", "AbstractModel.php"));
        supportingFiles.add(new SupportingFile("HelloFreshResponse.php", packagePath + "/lib", "HelloFreshResponse.php"));
        supportingFiles.add(new SupportingFile("AbstractHelloFreshPurpose.php", packagePath + "/lib", "AbstractHelloFreshPurpose.php"));
        supportingFiles.add(new SupportingFile("AbstractCollection.php", packagePath + "/lib", "AbstractCollection.php"));
        supportingFiles.add(new SupportingFile("ModelInterface.php", packagePath + "/lib", "ModelInterface.php"));
    }

    public Map<String, String> testTemplateFiles() {
        return testTemplateFiles;
    }

    @Override
    public CodegenModel fromModel(String name, Model model) {
        CodegenModel codeModel = super.fromModel(name, model);

        codeModel.variableName = this.toVariableName(name);
        codeModel.classname = this.toClassName(name);
        codeModel.namespaceName = this.toNamespaceName(name);

        return codeModel;
    }

    @Override
    public String toApiName(String name) {
        if(name.length() == 0)
            return "Default";
        return initialCaps(name);
    }

    @Override
    public String escapeReservedWord(String name) {
        return "_" + name;
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + "/" + apiPackage().replace('.', File.separatorChar);
    }

    public String modelFileFolder() {
        return outputFolder + "/" + modelPackage().replace('.', File.separatorChar);
    }

    @Override
    public String getTypeDeclaration(Property p) {
        if(p instanceof ArrayProperty) {
            ArrayProperty ap = (ArrayProperty) p;
            Property inner = ap.getItems();
            return getSwaggerType(p) + "[" + getTypeDeclaration(inner) + "]";
        } else if (p instanceof MapProperty) {
            MapProperty mp = (MapProperty) p;
            Property inner = mp.getAdditionalProperties();
            return getSwaggerType(p) + "[string," + getTypeDeclaration(inner) + "]";
        }
        String fqcn = super.getTypeDeclaration(p);

        if(fqcn.contains("ApiModel")){
            return this.toFqcnName(fqcn);
        }
        return fqcn;
    }

    @Override
    public String getSwaggerType(Property p) {
        String swaggerType = super.getSwaggerType(p);
        String type = null;

        if(typeMapping.containsKey(swaggerType)) {
            type = typeMapping.get(swaggerType);

            if(languageSpecificPrimitives.contains(type)) {
                return type;
            }
        } else
            type = swaggerType;

        if(type == null)
            return null;

        return type;
    }

    public String toDefaultValue(Property p) {
        return "null";
    }


    @Override
    public String toVarName(String name) {
        // parameter name starting with number won't compile
        // need to escape it by appending _ at the beginning
        if (name.matches("^[0-9]")) {
            name = "_" + name;
        }

        return name;
    }

    @Override
    public String toParamName(String name) {
        // should be the same as variable name
        return toVarName(name);
    }

    public String toFqcnName(String name){
        return this.toNamespaceName(name) + "\\" + this.toClassName(name);
    }

    public String toClassName(String name) {
        String[] parts = name.split("\\\\");
        return parts[(parts.length-1)];
    }

    public String toVariableName(String name) {
        char c[] = name.toCharArray();
        c[0] += 32;
        return new String(c);
    }

    public String toNamespaceName(String name) {

        List<String> parts = new ArrayList(Arrays.asList(name.split("\\\\")));

    //ApiModel\Subscription\Product\Product
    //Subscription\Product

        parts.remove(0);
        parts.remove(parts.size()-1);
        parts.add(0, "Entities");
        parts.add(0, "HelloFreshClient");
        parts.add(0, "HelloFresh");

        String fqcn = "";
        for(String part: parts) {
            fqcn += part+"\\";
        }

        return fqcn.substring(0, fqcn.length()-1);
    }

    @Override
    public String toModelName(String name) {
        // model name cannot use reserved keyword
        if(reservedWords.contains(name))
            escapeReservedWord(name); // e.g. return => _return

        // camelize the model name
        // phone_number => PhoneNumber
        return camelize(name);
    }

    @Override
    public String toModelFilename(String name) {
        // should be the same as the model name
        return name.replace("\\","/").replace("ApiModel/","");
    }

}
