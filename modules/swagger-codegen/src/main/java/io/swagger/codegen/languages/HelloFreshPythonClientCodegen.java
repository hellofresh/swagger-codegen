// package com.wordnik.swagger.codegen.languages;
//
// import com.wordnik.swagger.codegen.*;
// import com.wordnik.swagger.models.*;
// import com.wordnik.swagger.models.properties.*;
//
// import java.io.File;
// import java.util.*;
//
// public class HelloFreshPythonClientCodegen extends DefaultCodegen implements CodegenConfig {
//   String module = "client";
//
//   public CodegenType getTag() {
//     return CodegenType.CLIENT;
//   }
//
//   public String getName() {
//     return "python";
//   }
//
//   public String getHelp() {
//     return "Generates a Python client library.";
//   }
//
//   public HelloFreshPythonClientCodegen() {
//     super();
//     outputFolder = "generated-code/hellofreshpython";
//     modelTemplateFiles.put("model.mustache", ".py");
//     apiTemplateFiles.put("api.mustache", ".py");
//     templateDir = "python";
//
//     apiPackage = module;
//     modelPackage = module + ".models";
//
//     languageSpecificPrimitives.clear();
//     languageSpecificPrimitives.add("int");
//     languageSpecificPrimitives.add("float");
//     languageSpecificPrimitives.add("long");
//     languageSpecificPrimitives.add("list");
//     languageSpecificPrimitives.add("bool");
//     languageSpecificPrimitives.add("str");
//     languageSpecificPrimitives.add("datetime");
//     languageSpecificPrimitives.add("object");
//
//
//     typeMapping.clear();
//     typeMapping.put("integer", "int");
//     typeMapping.put("float", "float");
//     typeMapping.put("long", "long");
//     typeMapping.put("double", "float");
//     typeMapping.put("array", "list");
//     typeMapping.put("map", "map");
//     typeMapping.put("boolean", "bool");
//     typeMapping.put("string", "str");
//     typeMapping.put("date", "datetime");
//     typeMapping.put("DateTime", "datetime");
//     typeMapping.put("Doctrine\\Common\\Collections\\ArrayCollection\\ArrayCollection", "dict");
//
//
//     // from https://docs.python.org/release/2.5.4/ref/keywords.html
//     reservedWords = new HashSet<String> (
//       Arrays.asList(
//         "and", "del", "from", "not", "while", "as", "elif", "global", "or", "with",
//         "assert", "else", "if", "pass", "yield", "break", "except", "import",
//         "print", "class", "exec", "in", "raise", "continue", "finally", "is",
//         "return", "def", "for", "lambda", "try"));
//
//     supportingFiles.add(new SupportingFile("README.mustache", module, "README.md"));
//     supportingFiles.add(new SupportingFile("swagger.mustache", module, "swagger.py"));
//     supportingFiles.add(new SupportingFile("__init__.mustache", module, "__init__.py"));
//     supportingFiles.add(new SupportingFile("__init__.mustache", modelPackage.replace('.', File.separatorChar), "__init__.py"));
//   }
//
//   @Override
//   public CodegenModel fromModel(String name, Model model) {
//     CodegenModel codeModel = super.fromModel(name, model);
//
//     codeModel.classname = this.toClassName(name);
//     return codeModel;
//
//   }
//
//   public String toClassName(String name) {
//     String[] parts = name.split("\\\\");
//     return parts[(parts.length-1)];
//   }
//
//   @Override
//   public String toModelFilename(String name) {
//     int endIndex = name.lastIndexOf("\\");
//     //return name.replace("\\","/").replace("ApiModel/","");
//     return name.substring(endIndex+1);
//   }
//
//   @Override
//   public String escapeReservedWord(String name) {
//     return "_" + name;
//   }
//
//   @Override
//   public String apiFileFolder() {
//     return outputFolder + "/" + apiPackage().replace('.', File.separatorChar);
//   }
//
//   public String modelFileFolder() {
//     return outputFolder + "/" + modelPackage().replace('.', File.separatorChar);
//   }
//
//   @Override
//   public String getTypeDeclaration(Property p) {
//     if(p instanceof ArrayProperty) {
//       ArrayProperty ap = (ArrayProperty) p;
//       Property inner = ap.getItems();
//       return getSwaggerType(p) + "[" + getTypeDeclaration(inner) + "]";
//     }
//     else if (p instanceof MapProperty) {
//       MapProperty mp = (MapProperty) p;
//       Property inner = mp.getAdditionalProperties();
//
//       return getSwaggerType(p) + "(String, " + getTypeDeclaration(inner) + ")";
//     }
//     return super.getTypeDeclaration(p);
//   }
//
//   @Override
//   public String getSwaggerType(Property p) {
//     String swaggerType = super.getSwaggerType(p);
//     String type = null;
//     if(typeMapping.containsKey(swaggerType)) {
//       type = typeMapping.get(swaggerType);
//       if(languageSpecificPrimitives.contains(type)) {
//         return type;
//       }
//     }
//     else
//       type = swaggerType;
//     return type;
//   }
//
//   public String toDefaultValue(Property p) {
// 	// TODO: Support Python def value
//     return "null";
//   }
// }
