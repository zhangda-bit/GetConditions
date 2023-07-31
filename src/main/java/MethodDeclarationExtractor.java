import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class MethodDeclarationExtractor extends VoidVisitorAdapter<List<MethodDeclaration>> {
    @Override
    public void visit(MethodDeclaration methodDeclaration, List<MethodDeclaration> collector) {
        super.visit(methodDeclaration, collector);
        collector.add(methodDeclaration);
    }
}
