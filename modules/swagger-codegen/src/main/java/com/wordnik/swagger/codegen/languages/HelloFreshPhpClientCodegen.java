package com.wordnik.swagger.codegen.languages;

import com.wordnik.swagger.codegen.*;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.models.properties.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.File;

public class HelloFreshPhpClientCodegen extends DefaultCodegen implements CodegenConfig {
  Logger LOGGER = LoggerFactory.getLogger(HelloFreshPhpClientCodegen.class);
  protected String invokerPackage = "com.wordnik.client";
  protected String groupId = "com.wordnik";
  protected String artifactId = "swagger-client";
  protected String artifactVersion = "1.0.0";
  protected Map<String, String> testTemplateFiles = new HashMap<String, String>();

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

    //TODO determine hte package name from host name
    invokerPackage = camelize("HelloFreshPhpClient");

    String packagePath = invokerPackage + "-php";

    modelPackage = packagePath + "/lib/Entity";
    apiPackage = packagePath + "/lib/Purpose";
    outputFolder = "generated-code/hellofreshphp";
    modelTemplateFiles.put("model.mustache", ".php");
    apiTemplateFiles.put("api.mustache", ".php");
    testTemplateFiles.put("test.mustache", ".php");
    templateDir = "php";

    typeMapping.clear();
    languageSpecificPrimitives.clear();

    reservedWords = new HashSet<String> (
      Arrays.asList(
        "__halt_compiler", "abstract", "and", "array", "as", "break", "callable", "case", "catch", "class", "clone", "const", "continue", "declare", "default", "die", "do", "echo", "else", "elseif", "empty", "enddeclare", "endfor", "endforeach", "endif", "endswitch", "endwhile", "eval", "exit", "extends", "final", "for", "foreach", "function", "global", "goto", "if", "implements", "include", "include_once", "instanceof", "insteadof", "interface", "isset", "list", "namespace", "new", "or", "print", "private", "protected", "public", "require", "require_once", "return", "static", "switch", "throw", "trait", "try", "unset", "use", "var", "while", "xor")
    );

    additionalProperties.put("invokerPackage", invokerPackage);
    additionalProperties.put("groupId", groupId);
    additionalProperties.put("artifactId", artifactId);
    additionalProperties.put("artifactVersion", artifactVersion);

    languageSpecificPrimitives.add("int");
    languageSpecificPrimitives.add("array");
    languageSpecificPrimitives.add("map");
    languageSpecificPrimitives.add("string");
    languageSpecificPrimitives.add("DateTime");

    typeMapping.put("long", "int");
    typeMapping.put("integer", "int");
    typeMapping.put("Array", "array");
    typeMapping.put("String", "string");
    typeMapping.put("List", "array");
    typeMapping.put("map", "map");

    supportingFiles.add(new SupportingFile("HelloFreshClient.mustache", packagePath + "/lib", "HelloFreshClient.php"));
    supportingFiles.add(new SupportingFile("Deserializer.php", packagePath + "/lib", "Deserializer.php"));
    supportingFiles.add(new SupportingFile("AbstractModel.php", packagePath + "/lib", "AbstractModel.php"));
    supportingFiles.add(new SupportingFile("HelloFreshResponse.php", packagePath + "/lib", "HelloFreshResponse.php"));
    supportingFiles.add(new SupportingFile("AbstractHelloFreshPurpose.php", packagePath + "/lib", "AbstractHelloFreshPurpose.php"));
    supportingFiles.add(new SupportingFile("AbstractCollection.php", packagePath + "/lib", "AbstractCollection.php"));
    supportingFiles.add(new SupportingFile("ModelInterface.php", packagePath + "/lib", "ModelInterface.php"));
    supportingFiles.add(new SupportingFile("HelloFreshClientInterface.php", packagePath + "/lib", "HelloFreshClientInterface.php"));
    supportingFiles.add(new SupportingFile("HelloFreshResponseInterface.php", packagePath + "/lib", "HelloFreshResponseInterface.php"));
    supportingFiles.add(new SupportingFile("DeserializerInterface.php", packagePath + "/lib", "DeserializerInterface.php"));
    supportingFiles.add(new SupportingFile("SwaggerTypesResolver.php", packagePath + "/lib", "SwaggerTypesResolver.php"));
    supportingFiles.add(new SupportingFile("TypesResolverInterface.php", packagePath + "/lib", "TypesResolverInterface.php"));
    supportingFiles.add(new SupportingFile("Exception/HelloFreshClientException.php", packagePath + "/lib/Exception", "HelloFreshClientException.php"));
    supportingFiles.add(new SupportingFile("Exception/MissingClassException.php", packagePath + "/lib/Exception", "MissingClassException.php"));
  }

  public Map<String, String> testTemplateFiles() {
    return testTemplateFiles;
  }

  @Override
  public CodegenModel fromModel(String name, Model model) {
    CodegenModel codeModel = super.fromModel(name, model);

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
    }
    else if (p instanceof MapProperty) {
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
    String swaggerType = null;
    if(p instanceof StringProperty)
      swaggerType = "string";
    else if (p instanceof BooleanProperty)
      swaggerType = "boolean";
    else if(p instanceof DateProperty)
      swaggerType = "date";
    else if(p instanceof DateTimeProperty)
      swaggerType = "DateTime";
    else if (p instanceof DoubleProperty)
      swaggerType = "double";
    else if (p instanceof FloatProperty)
      swaggerType = "float";
    else if (p instanceof IntegerProperty)
      swaggerType = "integer";
    else if (p instanceof LongProperty)
      swaggerType = "long";
    else if (p instanceof MapProperty)
      swaggerType = "map";
    else if (p instanceof DecimalProperty)
      swaggerType = "number";
    else if (p instanceof RefProperty) {
      RefProperty r = (RefProperty)p;
      swaggerType = r.get$ref();
      if(swaggerType.indexOf("#/definitions/") == 0)
        swaggerType = swaggerType.substring("#/definitions/".length());
    }
    else {
      if(p != null) swaggerType = p.getType();
    }

    String type = null;
    if(typeMapping.containsKey(swaggerType)) {
      type = typeMapping.get(swaggerType);
      if(languageSpecificPrimitives.contains(type)) {
        return type;
      }
    }
    else
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

  public String toNamespaceName(String name) {

    List<String> parts = new ArrayList(Arrays.asList(name.split("\\\\")));

    //ApiModel\Subscription\Product\Product
    //Subscription\Product

    parts.remove(0);
    parts.remove(parts.size()-1);
    parts.add(0, "Entity");
    parts.add(0, "HelloFreshClient");
    parts.add(0, "HelloFresh");

    String fqcn = "";
    for(String part: parts) {
        fqcn += part+"\\";
    }

    return "\\" + fqcn.substring(0, fqcn.length()-1);
  }

  @Override
  public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Model> definitions) {
      CodegenOperation op = super.fromOperation(path, httpMethod, operation, definitions);

      op.lcHttpMethod = httpMethod.toLowerCase();

      return op;
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
