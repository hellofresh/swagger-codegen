package com.wordnik.swagger.codegen.languages;

import com.wordnik.swagger.codegen.*;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.models.properties.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.File;

public class HellofreshPhpClientCodegen extends DefaultCodegen implements CodegenConfig {
  Logger LOGGER = LoggerFactory.getLogger(HellofreshPhpClientCodegen.class);
  protected String invokerPackage = "com.wordnik.client";
  protected String groupId = "com.wordnik";
  protected String artifactId = "swagger-client";
  protected String artifactVersion = "1.0.0";

  public CodegenType getTag() {
    return CodegenType.CLIENT;
  }

  public String getName() {
    return "php";
  }

  public String getHelp() {
    return "Generates a PHP client library.";
  }

  public HellofreshPhpClientCodegen() {
    super();

    //TODO determine hte package name from host name
    invokerPackage = camelize("HelloFreshPhpClient");

    String packagePath = invokerPackage + "-php";

    modelPackage = packagePath + "/lib/Models";
    apiPackage = packagePath + "/lib";
    outputFolder = "generated-code/hellofreshphp";
    modelTemplateFiles.put("model.mustache", ".php");
    apiTemplateFiles.put("api.mustache", ".php");
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

    LOGGER.warn("##########" + packagePath);
    supportingFiles.add(new SupportingFile("APIClient.mustache", packagePath + "/lib", "APIClient.php"));
    supportingFiles.add(new SupportingFile("APIClientException.mustache", packagePath + "/lib", "APIClientException.php"));
  }

  @Override
  public CodegenModel fromModel(String name, Model model) {
    CodegenModel codeModel = super.fromModel(name, model);

    codeModel.classname = this.toClassName(name);
    codeModel.namespaceName = this.toNamespaceName(name);

    return codeModel;

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
    String swaggerType = super.getSwaggerType(p);
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

    // return the name in underscore style
    // PhoneNumber => phone_number
    return underscore(name);
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
    parts.add(0, "Models");
    parts.add(0, "PhpClient");
    parts.add(0, "Api");
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
    //LOGGER.info("FILE PATH: "+name);
    //return toModelName(name);
  }

}
