
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import bolts.WordCounter;
import bolts.WordNormalizer;
import spouts.WordReader;

public class TopologyMain {

	public static void main(String[] args) throws InterruptedException, AlreadyAliveException, InvalidTopologyException {
		// 定义一个Topology
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader", new WordReader());
		builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
		//随机分组
		builder.setBolt("word-counter", new WordCounter(), 2).shuffleGrouping("word-normalizer");

		//字段分组
//		builder.setBolt("word-counter", new WordCounter(), 2).fieldsGrouping("word-normalizer", new Fields("word"));
		// 配置
		Config conf = new Config();
//		conf.put("wordsFile", args[0]);
//		
//		args = new String[]{"/Users/PC/Desktop/word.txt", "result.txt"};
//		args = new String[]{"/Users/PC/Desktop/word.txt", "/home/parallels/result.txt", "wordcount"};
//		args = new String[]{"/Users/PC/Desktop/word.txt", "/Users/PC/Desktop/result.txt", "wordcount"};
		args = new String[]{"/home/parallels/word.txt", "/home/parallels/result.txt", "wordcount"};
		args = new String[]{"/Users/PC/Desktop/word.txt", "/Users/PC/Desktop/result.txt"};
//		args = new String[]{"/src/main/resources/words.txt", "/Users/PC/Desktop/result.txt"};

		conf.put("wordsFile", args[0]);
		conf.put("outFile", args[1]);
		conf.put(Config.STORM_MESSAGING_NETTY_MIN_SLEEP_MS, 1);  

		conf.setDebug(false);
		// 提交Topology
		if (args != null && args.length == 3) {
			conf.setNumWorkers(2);
			StormSubmitter.submitTopology(args[2], conf, builder.createTopology());
		} else {
			conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
			// 创建一个本地模式cluster
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("Getting-Started-Toplogie", conf, builder.createTopology());
			Thread.sleep(10000);
//			cluster.killTopology("Getting-Started-Toplogie");
			cluster.shutdown();
			System.out.println("end");
		}
	}
}