package jig.infrastructure.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import jig.domain.model.japanasename.JapaneseName;
import jig.domain.model.japanasename.JapaneseNameRepository;
import jig.domain.model.thing.Name;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

@Component
public class ClassCommentReader {

    private final JapaneseNameRepository repository;

    public ClassCommentReader(JapaneseNameRepository repository) {
        this.repository = repository;
    }

    public void execute(Path path) {
        if (!path.toString().endsWith(".java")) {
            return;
        }

        try {
            CompilationUnit cu = JavaParser.parse(path);
            com.github.javaparser.ast.expr.Name packageName = cu.accept(new GenericVisitorAdapter<com.github.javaparser.ast.expr.Name, Void>() {
                @Override
                public com.github.javaparser.ast.expr.Name visit(PackageDeclaration n, Void arg) {
                    return n.getName();
                }
            }, null);

            cu.accept(new VoidVisitorAdapter<com.github.javaparser.ast.expr.Name>() {
                @Override
                public void visit(ClassOrInterfaceDeclaration n, com.github.javaparser.ast.expr.Name packageName) {
                    Name fullQualifiedName = new Name(
                            packageName.asString() + "." + n.getNameAsString()
                    );

                    n.accept(new VoidVisitorAdapter<Name>() {
                        @Override
                        public void visit(JavadocComment n, Name name) {
                            String text = n.parse().getDescription().toText();
                            JapaneseName japaneseName = new JapaneseName(text);
                            repository.register(name, japaneseName);
                        }
                    }, fullQualifiedName);
                }
            }, packageName);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
