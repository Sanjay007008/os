
import java.util.*;

class sjf {

	static int z = 1000000007;
	static int sh = 100000;

	static class util {

		
		int id;
		// Arrival time
		int at;
		// Burst time
		int bt;
		// Completion time
		int ct;
		// Turnaround time
		int tat;
		// Waiting time
		int wt;
	}

	
	static util[] ar = new util[sh + 1];
	static {
		for (int i = 0; i < sh + 1; i++) {
			ar[i] = new util();
		}
	}

	static class util1 {

		
		int p_id;
		
		int bt1;
	};

	static util1 range = new util1();

	
	static util1[] tr = new util1[4 * sh + 5];
	static {
		for (int i = 0; i < 4 * sh + 5; i++) {
			tr[i] = new util1();
		}
	}

	
	static int[] mp = new int[sh + 1];

	
	static void update(int node, int st, int end, 
						int ind, int id1, int b_t)
	{
		if (st == end) {
			tr[node].p_id = id1;
			tr[node].bt1 = b_t;
			return;
		}
		int mid = (st + end) / 2;
		if (ind <= mid)
			update(2 * node, st, mid, ind, id1, b_t);
		else
			update(2 * node + 1, mid + 1, end, ind, id1, b_t);
		if (tr[2 * node].bt1 < tr[2 * node + 1].bt1) {
			tr[node].bt1 = tr[2 * node].bt1;
			tr[node].p_id = tr[2 * node].p_id;
		} else {
			tr[node].bt1 = tr[2 * node + 1].bt1;
			tr[node].p_id = tr[2 * node + 1].p_id;
		}
	}

	
	static util1 query(int node, int st, int end,
						int lt, int rt) 
	{
		if (end < lt || st > rt)
			return range;
		if (st >= lt && end <= rt)
			return tr[node];
		int mid = (st + end) / 2;
		util1 lm = query(2 * node, st, mid, lt, rt);
		util1 rm = query(2 * node + 1, mid + 1, end, lt, rt);
		if (lm.bt1 < rm.bt1)
			return lm;
		return rm;
	}

	
	static void non_preemptive_sjf(int n) {

		
		int counter = n;

		
		int upper_range = 0;

		
		int tm = Math.min(Integer.MAX_VALUE, ar[upper_range + 1].at);

		
		while (counter != 0) {
			for (; upper_range <= n;) {
				upper_range++;
				if (ar[upper_range].at > tm || upper_range > n) {
					upper_range--;
					break;
				}

				update(1, 1, n, upper_range, ar[upper_range].id, 
						ar[upper_range].bt);
			}

			
			util1 res = query(1, 1, n, 1, upper_range);

			
			if (res.bt1 != Integer.MAX_VALUE) {
				counter--;
				int index = mp[res.p_id];
				tm += (res.bt1);

				
				ar[index].ct = tm;
				ar[index].tat = ar[index].ct - ar[index].at;
				ar[index].wt = ar[index].tat - ar[index].bt;

				
				update(1, 1, n, index, Integer.MAX_VALUE, Integer.MAX_VALUE);
			} else {
				tm = ar[upper_range + 1].at;
			}
		}
	}

	
	static void execute(int n) {

		
		Arrays.sort(ar, 1, n, new Comparator<util>() {
			public int compare(util a, util b) {
				if (a.at == b.at)
					return a.id - b.id;
				return a.at - b.at;
			}
		});
		for (int i = 1; i <= n; i++)
			mp[ar[i].id] = i;

		
		non_preemptive_sjf(n);
	}

	
	static void print(int n) {

		System.out.println("ProcessId Arrival Time Burst Time" +
				" Completion Time Turn Around Time Waiting Time");
		for (int i = 1; i <= n; i++) {
			System.out.printf("%d\t\t%d\t\t%d\t\t%d\t\t%d\t\t%d\n", 
				ar[i].id, ar[i].at, ar[i].bt, ar[i].ct, ar[i].tat,
					ar[i].wt);
		}
	}

	
	public static void main(String[] args) 
	{
		
		int n = 5;

		
		range.p_id = Integer.MAX_VALUE;
		range.bt1 = Integer.MAX_VALUE;

		for (int i = 1; i <= 4 * sh + 1; i++)
		{
			tr[i].p_id = Integer.MAX_VALUE;
			tr[i].bt1 = Integer.MAX_VALUE;
		}

		
		ar[1].at = 1;
		ar[1].bt = 7;
		ar[1].id = 1;

		ar[2].at = 2;
		ar[2].bt = 5;
		ar[2].id = 2;

		ar[3].at = 3;
		ar[3].bt = 1;
		ar[3].id = 3;

		ar[4].at = 4;
		ar[4].bt = 2;
		ar[4].id = 4;

		ar[5].at = 5;
		ar[5].bt = 8;
		ar[5].id = 5;

		execute(n);

		
		print(n);
	}
}



