package com.tcb.auto.utils;

import java.util.List;

/**
 * This class implements an Assertion in Automation Framework. It supports 8
 * types of assertion including:
 * <ul>
 * <li>assert equal <code>a == b</code></li>
 * <li>assert not equal <code>a != b</code></li>
 * <li>assert greater <code>a > b</code></li>
 * <li>assert smaller <code>a < b </code></li>
 * <li>assert not greater <code>a <= b </code></li>
 * <li>assert not smaller <code>a >= b </code></li>
 * <li>assert belong to <code>a (- b</code></li>
 * <li>assert contain <code>a -) b</code></li>
 * </ul>
 * 
 * @author bachtx2
 * @since JDK1.8
 * @version 1.0.170418
 *
 */
public class AssertionEvaluator {

	/**
	 * Check if two matrixes have the same set of rows. Differences in the order
	 * of rows is ignored
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean assertEqual(String source, String target) {
		Commons.getLogger().debug("Assert '" + source +"' equal '" + target + "'");
		System.out.println("Assert '" + source +"' equal '" + target + "'");
		return (new MatrixString()).compare(source, target, Constants.SEP_1, Constants.SEP_2);
	}

	public boolean assertEqualIgnoreCase(String source, String target) {
		Commons.getLogger().debug("Assert '" + source +"' equal ignore case '" + target + "'");
		System.out.println("Assert '" + source +"' equal ignore case '" + target + "'");
		return (new MatrixString()).compare(source.toLowerCase(), target.toLowerCase(), Constants.SEP_1, Constants.SEP_2);
	}
	
	/**
	 * compare amount
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean assertEqual(double source, double target) {
		Commons.getLogger().debug("Assert '" + source +"' equal '" + target + "'");
		return (new MatrixString()).compare(String.valueOf(source), String.valueOf(target), Constants.SEP_1, Constants.SEP_2);
	}
	public boolean assertApporoximate(double source, double target) {
		if(Math.abs(source-target)/source<0.02)
			return true;
		return false;
	}
	/**
	 * compare two list amount
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean assertEqual(List<String>source, List<String> target) {
		System.out.println("List Double source "+source +"	List double des: "+target);
		boolean result = true;
		if(source.size()!=target.size())
			return false;
		for (int i =0;i<source.size();i++) {
			double amountSource = Double.parseDouble(source.get(i));
			boolean resultTemp=false;
			for(int j=0;j<target.size();j++) {
				double amountTarget = Double.parseDouble(target.get(j));
				if(amountSource==amountTarget)
					resultTemp = true;
			}
			result = result&&resultTemp;
		}
		return result;
	}

	public boolean assertApporoximate(List<String>source, List<String> target) {
		System.out.println("List Double source "+source +"	List double des: "+target);
		boolean result = true;
		if(source.size()!=target.size())
			return false;
		for (int i =0;i<source.size();i++) {
			double amountSource = Double.parseDouble(source.get(i));
			boolean resultTemp=false;
			for(int j=0;j<target.size();j++) {
				double amountTarget = Double.parseDouble(target.get(j));
				if(Math.abs(amountSource-amountTarget)/amountSource<0.02)
					resultTemp = true;
			}
			result = result&&resultTemp;
		}
		return result;
	}

	/**
	 * compare two list string
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean assertEqualListStrings(List<String>source, List<String> target) {
		System.out.println("List source "+source +"	List des: "+target);
		if(source.size()!=target.size())
			return false;
		for (String text : source) {
			if (!target.contains(text)) {
				return false;
			}
		}
		return true;
	}
		
	/**
	 * Check if two matrixes have two different sets of rows. Differences in the
	 * order of rows is ignored
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean assertNotEqual(String source, String target) {
		Commons.getLogger().debug("Assert '" + source +"' not equal '" + target + "'");
		return !(new MatrixString()).compare(source, target, Constants.SEP_1, Constants.SEP_2);
	}

	/**
	 * Check if the source number is greater than the target number
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean assertGreater(String source, String target) {
		Commons.getLogger().debug("Assert '" + source +"' greater '" + target + "'");
		Double sourceNum = Double.valueOf(source);
		Double targetNum = Double.valueOf(target);
		return sourceNum > targetNum;
	}

	/**
	 * Check if the source number is smaller than the target number
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean assertSmaller(String source, String target) {
		Commons.getLogger().debug("Assert '" + source +"' smaller '" + target + "'");
		Double sourceNum = Double.valueOf(source);
		Double targetNum = Double.valueOf(target);
		return sourceNum < targetNum;
	}

	/**
	 * Check if the source number is not greater than the target number
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean assertNotGreater(String source, String target) {
		Commons.getLogger().debug("Assert '" + source +"' not greater '" + target + "'");
		Double sourceNum = Double.valueOf(source);
		Double targetNum = Double.valueOf(target);
		return sourceNum <= targetNum;
	}

	/**
	 * Check if the source number is not smaller than the target number
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public boolean assertNotSmaller(String source, String target) {
		Commons.getLogger().debug("Assert '" + source +"' not smaller '" + target + "'");
		Double sourceNum = Double.valueOf(source);
		Double targetNum = Double.valueOf(target);
		return sourceNum >= targetNum;
	}

	/**
	 * Check if the source is a element of the target
	 * 
	 * @param source
	 *            a matrix row
	 * @param target
	 *            a matrix
	 * @return
	 */
	public boolean assertBelong(String source, String target) {
		Commons.getLogger().debug("Assert '" + source +"' belong '" + target + "'");
		return (new MatrixString()).contain(target, source, Constants.SEP_1, Constants.SEP_2);
	}

	/**
	 * Check if the source contains a row which equals to the target row
	 * 
	 * @param source
	 *            a matrix
	 * @param target
	 *            a matrix row
	 * @return
	 */
	public boolean assertContain(String source, String target) {
		Commons.getLogger().debug("Assert '" + source +"' contain '" + target + "'");
		return (new MatrixString()).contain(source, target, Constants.SEP_1, Constants.SEP_2);
	}

	public boolean assertContain(List<String> source, String target) {
		Commons.getLogger().debug("Assert '" + source.toString() +"' contain '" + target + "'");
		return source.contains(target);
	}


}
