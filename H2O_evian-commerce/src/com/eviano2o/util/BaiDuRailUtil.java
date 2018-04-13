package com.eviano2o.util;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BaiDuRailUtil {
/*	public static void main(String[] args) {
        Point2D.Double ys = new Point2D.Double(116.396305, 39.92139);
        ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
        points.add(new Point2D.Double(116.387112, 39.920977));
        points.add(new Point2D.Double(116.385243, 39.913063));
        points.add(new Point2D.Double(116.394226, 39.917988));
        points.add(new Point2D.Double(116.401772, 39.921364));
        points.add(new Point2D.Double(116.41248, 39.927893));
        System.out.println(checkWithJdkGeneralPath(ys, points));
    }*/

	   /**
     * 判断点是否在多边形内部
     *
     * @param pointStr
     * @param railStr 【[{"lng":113.821499,"lat":22.778883},{"lng":113.811869,"lat":22.772218},{"lng":113.808276,"lat":22.760355},{"lng":113.804251,"lat":22.740758},{"lng":113.825523,"lat":22.736624},{"lng":113.867923,"lat":22.720091},{"lng":113.875685,"lat":22.730491},{"lng":113.878272,"lat":22.733358},{"lng":113.866917,"lat":22.737958},{"lng":113.874679,"lat":22.746357},{"lng":113.871948,"lat":22.751556},{"lng":113.873385,"lat":22.756755},{"lng":113.869792,"lat":22.765287},{"lng":113.854125,"lat":22.771818},{"lng":113.836159,"lat":22.782748}]】
     * @return
     */
    public static boolean checkWithJdkGeneralPathStrParam(String pointStr, String railStr) {
    	Point2D.Double ys = stringToDoublePoint(pointStr);
    	List<Point2D.Double> polygon = railStrToDoublePointList(railStr);
        return checkWithJdkGeneralPath(ys, polygon);
    }
    
    /** 多点JSON转换 */
    static List<Point2D.Double> railStrToDoublePointList(String railStr){
    	List<Point2D.Double> result = new ArrayList();
    	JSONArray railJson = JSONArray.fromObject(railStr);
    	for (java.util.Iterator tor=railJson.iterator(); tor.hasNext();) {
			JSONObject job = (JSONObject)tor.next();
			result.add(new Point2D.Double(job.getDouble("lng"), job.getDouble("lat")));
    	}
    	return result;
    }
    
    /** 点转换 */
    static Point2D.Double stringToDoublePoint(String pointStr){
    	return new Point2D.Double(Double.valueOf(pointStr.split(",")[0]), Double.valueOf(pointStr.split(",")[1]));
    }
	
    /**
     * 判断点是否在多边形内部
     *
     * @param point
     * @param polygon
     * @return
     */
    public static boolean checkWithJdkGeneralPath(Point2D.Double point, List<Point2D.Double> polygon) {
        GeneralPath p = new GeneralPath();
        Point2D.Double first = polygon.get(0);
        p.moveTo(first.x, first.y);
        for (Point2D.Double d : polygon) {
            p.lineTo(d.x, d.y);
        }
        p.lineTo(first.x, first.y);
        p.closePath();
        return p.contains(point);
    }
}
