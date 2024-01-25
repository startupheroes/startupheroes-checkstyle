package es.startuphero.checkstyle.checks.annotation;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.puppycrawl.tools.checkstyle.api.TokenTypes.PARAMETERS;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.PARAMETER_DEF;
import static es.startuphero.checkstyle.util.AnnotationUtils.hasAnnotation;
import static es.startuphero.checkstyle.util.CommonUtils.getChildsByType;
import static java.util.stream.Collectors.toList;

/**
 * @author huseyinaydin
 */
public class RequestBodyAnnotationCheck extends AbstractCheck {

  private static final String MSG_KEY = "request.body.annotation.check";

  private String postMapping;

  private String putMapping;

  private String patchMapping;

  private String requestBody;

  @Override
  public int[] getDefaultTokens() {
    return getAcceptableTokens();
  }

  @Override
  public int[] getAcceptableTokens() {
    return new int[] {TokenTypes.METHOD_DEF};
  }

  @Override
  public int[] getRequiredTokens() {
    return getAcceptableTokens();
  }

  @Override
  public void visitToken(DetailAST ast) {
    if (Stream.of(postMapping, putMapping, patchMapping).anyMatch(annotation -> hasAnnotation(ast, annotation))) {
      List<DetailAST> parameters = getChildsByType(ast, PARAMETERS).stream()
                                                                   .map(p -> getChildsByType(p, PARAMETER_DEF))
                                                                   .flatMap(Collection::stream)
                                                                   .filter(pd -> !hasAnnotation(pd, requestBody))
                                                                   .collect(toList());
      parameters.forEach(param -> log(param.getLineNo(), MSG_KEY, getParameterName(param)));
    }
  }

  private String getParameterName(DetailAST param) {
    return getChildsByType(param, TokenTypes.IDENT)
        .stream()
        .findFirst()
        .map(DetailAST::getText)
        .orElse("UNKNOWN");
  }

  public String getPostMapping() {
    return postMapping;
  }

  public void setPostMapping(String postMapping) {
    this.postMapping = postMapping;
  }

  public String getPutMapping() {
    return putMapping;
  }

  public void setPutMapping(String putMapping) {
    this.putMapping = putMapping;
  }

  public String getPatchMapping() {
    return patchMapping;
  }

  public void setPatchMapping(String patchMapping) {
    this.patchMapping = patchMapping;
  }

  public String getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(String requestBody) {
    this.requestBody = requestBody;
  }
}
