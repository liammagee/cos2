package models.ahp;

import Jama.Matrix;
import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import models.CoSEntityListener;
import models.CriticalIssue;
import models.AHPUtils;

import models.RdfModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * CosUser: E65691
 * Date: 17/05/11
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
@Namespaces({"frbr", "http://vocab.org/frbr/core#",
        "dc", "http://purl.org/dc/terms/",
        "cos", "http://circlesofsustainability.org/ontology#",
        "foaf", "http://xmlns.com/foaf/0.1/"})
@RdfsClass("cos:IssueMatrix")
@Entity
@EntityListeners(CoSEntityListener.class)
public class IssueMatrix extends RdfModel {
    
    @Id
    public Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @RdfProperty("cos:hasCriterion")
    private Criterion criterion;

    // @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    // @RdfProperty("cos:hasCriticalIssue")
    private List<CriticalIssue> criticalIssues;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @RdfProperty("cos:hasMatrixRow")
    protected List<MatrixRow> matrixRows = new ArrayList<MatrixRow>();


    public IssueMatrix(){}

    public IssueMatrix(Criterion criterion, List<CriticalIssue> criticalIssues, double[][] matrixArray) {
        this.criterion = criterion;
        this.criticalIssues = criticalIssues;
        setMatrixArray(matrixArray);
    }

    public Criterion getCriterion() {
        return criterion;
    }

    public void setCriterion(Criterion criterion) {
        this.criterion = criterion;
    }

    public List<CriticalIssue> getCriticalIssues() {
        return criticalIssues;
    }

    public void setCriticalIssues(List<CriticalIssue> criticalIssues) {
        this.criticalIssues = criticalIssues;
    }

    public List<MatrixRow> getMatrixRows() {
        return matrixRows;
    }

    public void setMatrixRows(List<MatrixRow> matrixRows) {
        this.matrixRows = matrixRows;
    }

    // AbstractMatrix methods. TODO: Figure out how to have inheritance super classes

    public double[][] getMatrixArray() {
        List<MatrixRow> matrixRows = getMatrixRows();
        double[][] cellValues = new double[matrixRows.size()][matrixRows.get(0).getCells().size()];
        int i = 0;
        for (Iterator<MatrixRow> iterator = matrixRows.iterator(); iterator.hasNext(); i++) {
            MatrixRow next = iterator.next();
            cellValues[i] = next.getCellsAsArray();
        }
        return cellValues;
    }


    public void setMatrixArray(double[][] matrixArray) {
        List<MatrixRow> matrixRows = new ArrayList<MatrixRow>();
        for (int i = 0; i < matrixArray.length; i++) {
            double[] doubles = matrixArray[i];
            MatrixRow row = new MatrixRow();
            for (int j = 0; j < doubles.length; j++) {
                double aDouble = doubles[j];
                MatrixCell cell = new MatrixCell();
                cell.setValue(aDouble);
                row.addCell(cell);
            }
            matrixRows.add(row);
        }
        setMatrixRows(matrixRows);
    }

    public Matrix getMatrixAsMatrix() {
        return new Matrix(getMatrixArray());
    }

    public double getPrincipalEigenvalue() {
        return AHPUtils.generatePrincipalEigenvalue(getMatrixAsMatrix());
    }

    public double[] getPrincipalEigenvector() {
        return AHPUtils.generateNormalisedPrincipalEigenvector(getMatrixAsMatrix());
    }

    public double getConsistencyIndex() {
        return AHPUtils.getConsistencyIndex(getMatrixAsMatrix());
    }

    public double getConsistencyRatio() {
        return AHPUtils.getConsistencyRatio(getMatrixAsMatrix());
    }

    public boolean isConsistent() {
        return AHPUtils.isConsistent(getMatrixAsMatrix());
    }
}
