package com.azure.digitrecognition;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class BuildModel {

	private Instances trainingSet;

	private String sumNB;
	private String matNB;
	private String sumJ48;
	private String matJ48;
	private String sumIBk;
	private String matIBk;

	public static void main(String[] args) throws Exception {
		BuildModel b = new BuildModel();
		b.createModel();
		b.usingNaiveBayes();
	}

	public void createModel() throws Exception {
		trainingSet = new Instances(new BufferedReader(new FileReader("res/digits.arff")));
		trainingSet.setClassIndex(trainingSet.numAttributes() - 1);

		NaiveBayes nb = new NaiveBayes();
		nb.buildClassifier(trainingSet);
		SerializationHelper.write("res/digits-nb.model", nb);

		J48 j48 = new J48();
		j48.buildClassifier(trainingSet);
		SerializationHelper.write("res/digits-j48.model", j48);

		IBk ibk = new IBk();
		ibk.buildClassifier(trainingSet);
		SerializationHelper.write("res/digits-ibk.model", ibk);
	}

	public void usingNaiveBayes() throws Exception {
		NaiveBayes nb = (NaiveBayes) SerializationHelper.read("res/digits-nb.model");

		Instances test = new Instances(new BufferedReader(new FileReader("res/test.arff")));
		test.setClassIndex(test.numAttributes()-1);

		Instance in = test.firstInstance();

		Evaluation eval = new Evaluation(trainingSet);

		eval.evaluateModel(nb, test);
		sumNB = eval.toSummaryString();
		sumNB += "\nPrecision : \t" + eval.precision(0) + "\nRecall : \t" + eval.recall(0);
		matNB = eval.toMatrixString();
		System.out.println(sumNB);
	}

	public String getNBSummary() {
		return sumNB;
	}

	public String getMatNB() {
		return matNB;
	}

	public void usingJ48() throws Exception {
		J48 j48 = (J48) SerializationHelper.read("res/digits-j48.model");

		Instances test = new Instances(new BufferedReader(new FileReader("res/test.arff")));
		test.setClassIndex(test.numAttributes() - 1);

		Instance in = test.firstInstance();

		Evaluation eval = new Evaluation(trainingSet);
		eval.evaluateModel(j48, test);
		sumJ48 = eval.toSummaryString();
		sumJ48 += "\nPrecision : \t" + eval.precision(0) + "\nRecall : \t" + eval.recall(0);
		matJ48 = eval.toMatrixString();
	}

	public String getJ48Summary() {
		return sumJ48;
	}

	public String getMatJ48() {
		return matJ48;
	}

	public void usingIBk() throws Exception {
		IBk ibk = (IBk) SerializationHelper.read("res/digits-ibk.model");

		Instances test = new Instances(new BufferedReader(new FileReader("res/test.arff")));
		test.setClassIndex(test.numAttributes()-1);

		Instance in = test.firstInstance();

		Evaluation eval = new Evaluation(trainingSet);
		eval.evaluateModel(ibk, test);
		sumIBk = eval.toSummaryString();
		sumIBk += "\nPrecision : \t" + eval.precision(0) + "\nRecall : \t" + eval.recall(0);
		matIBk = eval.toMatrixString();
	}

	public String getIBkSummary() {
		return sumIBk;
	}

	public String getMatIBk() {
		return matIBk;
	}
}