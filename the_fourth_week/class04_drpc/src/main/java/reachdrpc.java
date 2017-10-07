import backtype.storm.utils.DRPCClient;
public class reachdrpc {

	public static void main(String[] args) throws Exception {
		DRPCClient client = new DRPCClient("192.168.100.50", 3772);
		System.out.println(client.execute("reach", args[0]));

	}

}
