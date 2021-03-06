package io.swagger.codegen.languages;

import io.swagger.codegen.CliOption;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.DefaultCodegen;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenOperation;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.Model;
import io.swagger.models.properties.*;

import java.io.File;
import java.util.*;
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
    protected String packagePath = "HelloFreshPhpClient-php";
    protected String artifactVersion = "1.0.0";
    protected String srcBasePath = "lib";
    protected String variableNamingConvention= "snake_case";

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
        apiPackage = invokerPackage + "\\Purpose";
        modelPackage = invokerPackage + "\\Entity";

        supportingFiles.add(new SupportingFile("HelloFreshClient.mustache", packagePath + "/lib", "HelloFreshClient.php"));
        supportingFiles.add(new SupportingFile("Deserializer.php", packagePath + "/lib", "Deserializer.php"));
        supportingFiles.add(new SupportingFile("AbstractModel.php", packagePath + "/lib", "AbstractModel.php"));
        supportingFiles.add(new SupportingFile("HelloFreshResponse.php", packagePath + "/lib", "HelloFreshResponse.php"));
        supportingFiles.add(new SupportingFile("AbstractHelloFreshPurpose.php", packagePath + "/lib", "AbstractHelloFreshPurpose.php"));
        supportingFiles.add(new SupportingFile("AbstractCollection.php", packagePath + "/lib", "AbstractCollection.php"));
        supportingFiles.add(new SupportingFile("ModelInterface.php", packagePath + "/lib", "ModelInterface.php"));
        supportingFiles.add(new SupportingFile("HelloFreshClientInterface.php", packagePath + "/lib", "HelloFreshClientInterface.php"));
        supportingFiles.add(new SupportingFile("HelloFreshResponseInterface.php", packagePath + "/lib", "HelloFreshResponseInterface.php"));
    }

    public String getPackagePath() {
        return packagePath;
    }

    public String toPackagePath(String packageName, String basePath) {
        packageName = packageName.replace(invokerPackage, "");
        if (basePath != null && basePath.length() > 0) {
            basePath = basePath.replaceAll("[\\\\/]?$", "") + File.separatorChar;
        }

        String regFirstPathSeparator;
        if ("/".equals(File.separator)) { // for mac, linux
            regFirstPathSeparator = "^/";
        } else { // for windows
            regFirstPathSeparator = "^\\\\";
        }

        String regLastPathSeparator;
        if ("/".equals(File.separator)) { // for mac, linux
            regLastPathSeparator = "/$";
        } else { // for windows
            regLastPathSeparator = "\\\\$";
        }

        return (getPackagePath() + File.separatorChar + basePath
                    // Replace period, backslash, forward slash with file separator in package name
                    + packageName.replaceAll("[\\.\\\\/]", Matcher.quoteReplacement(File.separator))
                    // Trim prefix file separators from package path
                    .replaceAll(regFirstPathSeparator, ""))
                    // Trim trailing file separators from the overall path
                    .replaceAll(regLastPathSeparator+ "$", "");
    }

    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    public String getName() {
        return "php";
    }

    public String getHelp() {
        return "Generates a PHP client library.";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        if (additionalProperties.containsKey(PACKAGE_PATH)) {
            this.setPackagePath((String) additionalProperties.get(PACKAGE_PATH));
        } else {
            additionalProperties.put(PACKAGE_PATH, packagePath);
        }

        if (additionalProperties.containsKey(SRC_BASE_PATH)) {
            this.setSrcBasePath((String) additionalProperties.get(SRC_BASE_PATH));
        } else {
            additionalProperties.put(SRC_BASE_PATH, srcBasePath);
        }

        if (additionalProperties.containsKey(CodegenConstants.INVOKER_PACKAGE)) {
            this.setInvokerPackage((String) additionalProperties.get(CodegenConstants.INVOKER_PACKAGE));
        } else {
            additionalProperties.put(CodegenConstants.INVOKER_PACKAGE, invokerPackage);
        }

        if (!additionalProperties.containsKey(CodegenConstants.MODEL_PACKAGE)) {
            additionalProperties.put(CodegenConstants.MODEL_PACKAGE, modelPackage);
        }

        if (!additionalProperties.containsKey(CodegenConstants.API_PACKAGE)) {
            additionalProperties.put(CodegenConstants.API_PACKAGE, apiPackage);
        }

        if (additionalProperties.containsKey(COMPOSER_PROJECT_NAME)) {
            this.setComposerProjectName((String) additionalProperties.get(COMPOSER_PROJECT_NAME));
        } else {
            additionalProperties.put(COMPOSER_PROJECT_NAME, composerProjectName);
        }

        if (additionalProperties.containsKey(COMPOSER_VENDOR_NAME)) {
            this.setComposerVendorName((String) additionalProperties.get(COMPOSER_VENDOR_NAME));
        } else {
            additionalProperties.put(COMPOSER_VENDOR_NAME, composerVendorName);
        }

        if (additionalProperties.containsKey(CodegenConstants.ARTIFACT_VERSION)) {
            this.setArtifactVersion((String) additionalProperties.get(CodegenConstants.ARTIFACT_VERSION));
        } else {
            additionalProperties.put(CodegenConstants.ARTIFACT_VERSION, artifactVersion);
        }

        if (additionalProperties.containsKey(VARIABLE_NAMING_CONVENTION)) {
            this.setParameterNamingConvention((String) additionalProperties.get(VARIABLE_NAMING_CONVENTION));
        }

        additionalProperties.put("escapedInvokerPackage", invokerPackage.replace("\\", "\\\\"));
    }

    @Override
    public String escapeReservedWord(String name) {
        return "_" + name;
    }

    @Override
    public String apiFileFolder() {
        return (outputFolder + "/" + toPackagePath(apiPackage, srcBasePath));
    }

    public String modelFileFolder() {
        return (outputFolder + "/" + toPackagePath(modelPackage, srcBasePath));
    }

    @Override
    public String getTypeDeclaration(Property p) {
        if (p instanceof ArrayProperty) {
            ArrayProperty ap = (ArrayProperty) p;
            Property inner = ap.getItems();
            return getTypeDeclaration(inner) + "[]";
        } else if (p instanceof MapProperty) {
            MapProperty mp = (MapProperty) p;
            Property inner = mp.getAdditionalProperties();
            return getSwaggerType(p) + "[string," + getTypeDeclaration(inner) + "]";
        } else if (p instanceof RefProperty) {
            String type = super.getTypeDeclaration(p);
            return (!languageSpecificPrimitives.contains(type))
                    ? "\\" + modelPackage + "\\" + type : type;
        }
        return super.getTypeDeclaration(p);
    }

    @Override
    public String getTypeDeclaration(String name) {
        if (!languageSpecificPrimitives.contains(name)) {
            return "\\" + modelPackage + "\\" + name;
        }
        return super.getTypeDeclaration(name);
    }

    @Override
    public String getSwaggerType(Property p) {
        String swaggerType = super.getSwaggerType(p);
        String type = null;
        if (typeMapping.containsKey(swaggerType)) {
            type = typeMapping.get(swaggerType);
            if (languageSpecificPrimitives.contains(type)) {
                return type;
            } else if (instantiationTypes.containsKey(type)) {
                return type;
            }
        } else {
            type = swaggerType;
        }
        if (type == null) {
            return null;
        }
        return toModelName(type);
    }

    public void setInvokerPackage(String invokerPackage) {
        this.invokerPackage = invokerPackage;
    }

    public void setArtifactVersion(String artifactVersion) {
        this.artifactVersion = artifactVersion;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public void setSrcBasePath(String srcBasePath) {
        this.srcBasePath = srcBasePath;
    }

    public void setParameterNamingConvention(String variableNamingConvention) {
        this.variableNamingConvention = variableNamingConvention;
    }

    public void setComposerVendorName(String composerVendorName) {
        this.composerVendorName = composerVendorName;
    }

    public void setComposerProjectName(String composerProjectName) {
        this.composerProjectName = composerProjectName;
    }

    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Model> definitions, Swagger swagger) {
        CodegenOperation op = super.fromOperation(path, httpMethod, operation, definitions, swagger);

        op.lcHttpMethod = httpMethod.toLowerCase();

        if (op.httpMethod.equals("GET") && String.valueOf(op.path.charAt(op.path.length() - 1)) != "}") {
            LOGGER.info("################# returnType wollah");
            op.returnType += "Collection";
        }

        return op;
    }

    @Override
    public String toVarName(String name) {
        // sanitize name
        name = sanitizeName(name);

        if ("camelCase".equals(variableNamingConvention)) {
          // return the name in camelCase style
          // phone_number => phoneNumber
          name =  camelize(name, true);
        } else { // default to snake case
          // return the name in underscore style
          // PhoneNumber => phone_number
          name =  underscore(name);
        }

        // parameter name starting with number won't compile
        // need to escape it by appending _ at the beginning
        if (name.matches("^\\d.*")) {
            name = "_" + name;
        }

        return name;
    }

    @Override
    public String toParamName(String name) {
        // should be the same as variable name
        return toVarName(name);
    }

    @Override
    public String toModelName(String name) {
        // Note: backslash ("\\") is allowed for e.g. "\\DateTime"
        name = name.replaceAll("[^\\w\\\\]+", "_");

        // remove dollar sign
        name = name.replaceAll("$", "");

        // model name cannot use reserved keyword
        if (reservedWords.contains(name)) {
            escapeReservedWord(name); // e.g. return => _return
        }

        // camelize the model name
        // phone_number => PhoneNumber
        List<String> parts = new ArrayList(Arrays.asList(name.split("\\\\")));

        if (parts.size() <= 2) {
            return camelize(name);
        }

        parts.remove(0);
        parts.remove(1);

        String fqcn = "";
        for(String part: parts) {
            fqcn += part+"\\";
        }

        return fqcn.substring(0, fqcn.length() - 1);
    }

    public String getClassName(String name) {
        List<String> parts = new ArrayList(Arrays.asList(name.split("\\\\")));

        if (parts.size() == 1) {
            return name;
        }

        return parts.get(parts.size() - 1);
    }

    @Override
    public CodegenModel fromModel(String name, Model model, Map<String, Model> allDefinitions) {
        CodegenModel codegenModel = super.fromModel(name, model, allDefinitions);

        codegenModel.namespaceName = toNamespaceName(name);
        codegenModel.classname = getClassName(name);

        return codegenModel;
    }

    @Override
    public String toModelFilename(String name) {
        return toNamespaceFolder(name).replace("\\", "/").replace("HelloFresh/HelloFreshClient/Entity", "");
    }

    @Override
    public String toOperationId(String operationId) {
        // throw exception if method name is empty
        if (StringUtils.isEmpty(operationId)) {
            throw new RuntimeException("Empty method name (operationId) not allowed");
        }

        // method name cannot use reserved keyword, e.g. return
        if (reservedWords.contains(operationId)) {
            throw new RuntimeException(operationId + " (reserved word) cannot be used as method name");
        }

        return camelize(sanitizeName(operationId), true);
    }

    /**
     * Return the default value of the property
     *
     * @param p Swagger property object
     * @return string presentation of the default value of the property
     */
    @Override
    public String toDefaultValue(Property p) {
        if (p instanceof StringProperty) {
            StringProperty dp = (StringProperty) p;
            if (dp.getDefault() != null) {
                return "'" + dp.getDefault().toString() + "'";
            }
        } else if (p instanceof BooleanProperty) {
            BooleanProperty dp = (BooleanProperty) p;
            if (dp.getDefault() != null) {
                return dp.getDefault().toString();
            }
        } else if (p instanceof DateProperty) {
            // TODO
        } else if (p instanceof DateTimeProperty) {
            // TODO
        } else if (p instanceof DoubleProperty) {
            DoubleProperty dp = (DoubleProperty) p;
            if (dp.getDefault() != null) {
                return dp.getDefault().toString();
            }
        } else if (p instanceof FloatProperty) {
            FloatProperty dp = (FloatProperty) p;
            if (dp.getDefault() != null) {
                return dp.getDefault().toString();
            }
        } else if (p instanceof IntegerProperty) {
            IntegerProperty dp = (IntegerProperty) p;
            if (dp.getDefault() != null) {
                return dp.getDefault().toString();
            }
        } else if (p instanceof LongProperty) {
            LongProperty dp = (LongProperty) p;
            if (dp.getDefault() != null) {
                return dp.getDefault().toString();
            }
        }

        return null;
    }

    public String toVariableName(String name) {
        char c[] = name.toCharArray();
        c[0] += 32;
        return new String(c);
    }

    public String toNamespaceFolder(String name) {
        List<String> parts = new ArrayList(Arrays.asList(name.split("\\\\")));

        if (parts.size() <= 2) {
            return name;
        }

        parts.remove(0);
        parts.remove(parts.size() - 1);
        parts.add(0, "Entity");
        parts.add(0, "HelloFreshClient");
        parts.add(0, "HelloFresh");

        String fqcn = "";
        for(String part: parts) {
            fqcn += part+"\\";
        }

        return fqcn.substring(0, fqcn.length() - 1);
    }

    public String toNamespaceName(String name) {
        List<String> parts = new ArrayList(Arrays.asList(name.split("\\\\")));

        if (parts.size() <= 2) {
            return name;
        }

        parts.remove(0);
        parts.remove(parts.size() - 1);
        parts.remove(parts.size() - 1);
        parts.add(0, "Entity");
        parts.add(0, "HelloFreshClient");
        parts.add(0, "HelloFresh");

        String fqcn = "";
        for(String part: parts) {
            fqcn += part+"\\";
        }

        return fqcn.substring(0, fqcn.length() - 1);
    }

    public String toClassName(String name) {
        String[] parts = name.split("\\\\");
        return parts[(parts.length-1)];
    }

    @Override
    public String toApiName(String name) {
        if (name.length() == 0) {
            return "Default";
        }
        return initialCaps(name);
    }

}
