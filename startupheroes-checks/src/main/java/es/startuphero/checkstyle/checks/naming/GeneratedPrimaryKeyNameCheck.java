package es.startuphero.checkstyle.checks.naming;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;

import static es.startuphero.checkstyle.util.AnnotationUtil.hasAnnotation;
import static es.startuphero.checkstyle.util.ClassUtil.isEntity;
import static es.startuphero.checkstyle.util.StringUtil.isEmpty;
import static es.startuphero.checkstyle.util.VariableUtil.getNonStaticVariables;
import static es.startuphero.checkstyle.util.VariableUtil.getVariableName;

/**
 * @author ozlem.ulag
 */
public class GeneratedPrimaryKeyNameCheck extends AbstractCheck {

  /**
   * A key is pointing to the warning message text in "messages.properties" file.
   */
  private static final String MSG_KEY = "generated.primary.key.name";

  /**
   * set type annotation to understand that a class is an entity.
   */
  private String typeAnnotation;

  /**
   * set abstract type annotation to understand that a class is abstract.
   */
  private String abstractTypeAnnotation;

  /**
   * set id annotation to understand that a variable is primary key of entity.
   */
  private String idAnnotation;

  /**
   * set generated value annotation to understand that primary key is generated.
   */
  private String generatedValueAnnotation;

  /**
   * set convenient name for variable for generated primary key.
   */
  private String suggestedGeneratedPrimaryKeyName;

  public void setTypeAnnotation(String typeAnnotation) {
    this.typeAnnotation = typeAnnotation;
  }

  public void setAbstractTypeAnnotation(String abstractTypeAnnotation) {
    this.abstractTypeAnnotation = abstractTypeAnnotation;
  }

  public void setIdAnnotation(String idAnnotation) {
    this.idAnnotation = idAnnotation;
  }

  public void setGeneratedValueAnnotation(String generatedValueAnnotation) {
    this.generatedValueAnnotation = generatedValueAnnotation;
  }

  public void setSuggestedGeneratedPrimaryKeyName(String suggestedGeneratedPrimaryKeyName) {
    this.suggestedGeneratedPrimaryKeyName = suggestedGeneratedPrimaryKeyName;
  }

  @Override
  public int[] getDefaultTokens() {
    return getAcceptableTokens();
  }

  @Override
  public int[] getAcceptableTokens() {
    return new int[] {TokenTypes.CLASS_DEF};
  }

  @Override
  public int[] getRequiredTokens() {
    return getAcceptableTokens();
  }

  @Override
  public void visitToken(DetailAST ast) {
    assertions();
    if (isEntity(ast, typeAnnotation) || isEntity(ast, abstractTypeAnnotation)) {
      List<DetailAST> variables = getNonStaticVariables(ast);
      for (DetailAST variable : variables) {
        if (hasAnnotation(variable, idAnnotation) &&
            hasAnnotation(variable, generatedValueAnnotation)) {
          String generatedPrimaryKeyName = getVariableName(variable);
          if (!generatedPrimaryKeyName.equals(suggestedGeneratedPrimaryKeyName)) {
            log(variable.getLineNo(), MSG_KEY, suggestedGeneratedPrimaryKeyName);
          }
          break;
        }
      }
    }
  }

  private void assertions() {
    assert !isEmpty(typeAnnotation);
    assert !isEmpty(abstractTypeAnnotation);
    assert !isEmpty(idAnnotation);
    assert !isEmpty(generatedValueAnnotation);
    assert !isEmpty(suggestedGeneratedPrimaryKeyName);
  }
}
