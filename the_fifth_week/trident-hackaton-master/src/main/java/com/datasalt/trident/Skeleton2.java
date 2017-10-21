package com.datasalt.trident;

import java.io.IOException;
import java.util.Map;

import storm.trident.TridentTopology;
import storm.trident.operation.BaseFilter;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;

/**
 * Use this skeleton for starting your own topology that uses the Fake tweets generator as data source.
 * 
 * @author pere
 */
public class Skeleton2 {

	public static class PerActorTweetsFilter extends BaseFilter {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String actor;		
		
//		private int partitionIndex;		

		public PerActorTweetsFilter(String actor) {
			this.actor = actor;
		}

	    @Override
		public void prepare(Map conf, TridentOperationContext context) {
			// TODO Auto-generated method stub
//			super.prepare(conf, context);
//	    	this.partitionIndex = context.getPartitionIndex();
		}

		@Override
		public boolean isKeep(TridentTuple tuple) {
//			boolean filter = tuple.getString(0).equals(actor);
//			if (filter) {
//				System.out.println("I am partition [" + partitionIndex + 
//						"] and i have kept a tweet by :" + actor + " tuple" + tuple);
//			}
			return tuple.getString(0).equals(actor);
		}
		
	}
	
	public static StormTopology buildTopology(LocalDRPC drpc) throws IOException {
		FakeTweetsBatchSpout spout = new FakeTweetsBatchSpout();

//		TridentTopology topology = new TridentTopology();
//		topology.newStream("spout", spout)
//		.each(new Fields("actor", "text"), new PerActorTweetsFilter("dave"))
//		.each(new Fields("actor", "text"), new Utils.PrintFilter());

		TridentTopology topology = new TridentTopology();
		topology.newStream("spout", spout)
		.each(new Fields("actor", "text"), new PerActorTweetsFilter("dave"))
		.project(new Fields("text"))
		.each(new Fields("text"), new Utils.PrintFilter());
		
		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		Config conf = new Config();

		LocalDRPC drpc = new LocalDRPC();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("hackaton", conf, buildTopology(drpc));
	}
}
