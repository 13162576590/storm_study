package bolts;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WordCounter implements IRichBolt {
	
    private final Logger logger = LoggerFactory.getLogger(getClass());

	Integer id;
	String name;
	Map<String, Integer> counters;
	private OutputCollector collector;
	private BufferedWriter output;
	private Map conf;


	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		logger.info("*****************prepare()********************");

		this.counters = new HashMap<String, Integer>();
		this.collector = collector;
		this.name = context.getThisComponentId();
		this.id = context.getThisTaskId();
		this.conf = stormConf;
	}

//	@Override
//	public void execute(Tuple input) {
//		String str = input.getString(0);
//		if (!counters.containsKey(str)) {
//			counters.put(str, 1);
//		} else {
//			Integer c = counters.get(str) + 1;
//			counters.put(str, c);
//		}
//		// 确认成功处理一个tuple
//		collector.ack(input);
//	}
	
	@Override
	public void execute(Tuple input) {
		logger.info("*****************execute()********************");

		String str = input.getString(0);
		Integer count = counters.get(str);
		if (count == null) {
			count = 0;
		}
		count++;
		counters.put(str, count);
		Iterator<String> iterator = counters.keySet().iterator();
		
		try {
			//结果集输出路劲  parallels
//			output = new BufferedWriter(new FileWriter("/home/parallels/wordcount.txt", false));
			output = new BufferedWriter(new FileWriter(conf.get("outFile").toString(), false));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				output.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		while(iterator.hasNext()) {
			String next = iterator.next();
			try {
				System.out.println(next + ":" + counters.get(next) + " ");
				output.write(next + ":" + counters.get(next) + " ");
				output.newLine();
				output.flush();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * Topology执行完毕的清理工作，比如关闭连接、释放资源等操作都会写在这里 因为这只是个Demo，我们用它来打印我们的计数器
	 */
	@Override
	public void cleanup() {
		logger.info("*****************cleanup()********************");

		System.out.println("-- Word Counter [" + name + "-" + id + "] --");
		for (Map.Entry<String, Integer> entry : counters.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		counters.clear();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		logger.info("*****************declareOutputFields()********************");

		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		logger.info("*****************getComponentConfiguration()********************");

		// TODO Auto-generated method stub
		return null;
	}
}