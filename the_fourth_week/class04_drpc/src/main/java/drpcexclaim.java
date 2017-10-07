import backtype.storm.utils.DRPCClient;
public class drpcexclaim {

	public static void main(String[] args) throws Exception {
		//192.168.100.50是test01.kysd.cn的IP地址
		DRPCClient client = new DRPCClient("192.168.100.50", 3772);
		for (String word : new String[]{"hello","dataguru"}) {
			System.out.println(client.execute("exclamation", word));
		}

	}

}
