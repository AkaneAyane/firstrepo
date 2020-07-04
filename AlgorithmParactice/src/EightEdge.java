import java.util.Arrays;
import java.util.Scanner;

public class EightEdge {
    public static boolean[] edges;  //存储8条边的状态，用true表示1，用false表示0，不过也不是一点要表示成这样，刚开始想的时候感觉boolean简单点，其实用int表示也差不多
    public static int min=Integer.MAX_VALUE;//存储变换到8条边全部相同最少次数，初始化为int的最大值
    public static int[] records; //八条边一共有256种状态，将八条边的状态转化为十进制数即为对应的下标。每一个数组成员存的是从初始状态开始转换，转换到该成员下标对应状态最少的次数
    public static void main(String[] args){
        Scanner input=new Scanner(System.in);
        String edgeInput=input.nextLine();
        edges=new boolean[8];
        records=new int[256];
        Arrays.fill(records,Integer.MAX_VALUE); //records数组全部初始化为int最大值
        parseInput(edgeInput);
        records[convert()]=0;  //records中，将输入的初始状态对应的最少次数改为0
        for(int i=0;i<8;i++) {  //开始递归，分别从8条边的每一个条边开始
            Transform(i, 0);
        }
        System.out.println(min);
    }

    /**
     * 把字符串的输入转化成boolean数组，存入edges
     * @param input
     */
    public static void parseInput(String input){
        for(int i=0;i<8;i++){
            if(input.charAt(i)=='1'){
                edges[i]=true;
            }
            else{
                edges[i]=false;
            }
        }
    }

    /**
     * 把edges数组当前状态转化成十进制数
     * @return
     */
    public static int convert(){
        int base=1;
        int sum=0;
        for(int i=0;i<8;i++){
            if(edges[i]){
                sum+=base;
            }
            base*=2;
        }
        return sum;
    }

    /**
     * 回溯法的递归过程
     * @param index 每次变换的中间的边的下标
     * @param times 进入这次递归前，已经进行过的变换次数
     */
    public static void Transform(int index,int times){
        if(times>=min){ //如果目前的变换次数已经比之前完成变换的最小次数还大，那么直接返回，不再递归
            return;
        }
        int decimal=convert();  //把当前状态转化为十进制
        if(decimal==0||decimal==255){   //如果转化结果为0或者255代表全部为0或全部为1，即变换完成
            if(times<min){      //如果次数比现在的最小次数还小则更新，否则忽略
                min=times;
            }
        }
        edges[index]=!edges[index];     //回溯法，假设将index周围的三条边变换
        edges[(index+1)%8]=!edges[(index+1)%8];     //用%运算确保下标0的边和7的边能连在一起
        edges[(index+7)%8]=!edges[(index+7)%8];     //用index+7代替了index-1进行%运算，因为好像负数%运算还是负数
        times++;//变换次数加1
        if(records[convert()]<=times){  //在records数组里查找，在之前的递归中是否曾经到达过这次变换过程之后的状态，已经之前需要的变换次数，如果现在的次数大于原来的次数，则代表目前的变换肯定不是最小的了，用原来的方法到达现在的状态次数更少，于是当前的递归可以结束了
            edges[index]=!edges[index];     //回溯法，回溯到未变化前的状态
            edges[(index+1)%8]=!edges[(index+1)%8];
            edges[(index+7)%8]=!edges[(index+7)%8];
            times--;
            return;
        }
        else{
            records[convert()]=times;   //如果次数小于原来的次数，则更新records表
        }
        for(int i=0;i<7;i++){   //对index边变换后，继续在除去index边(如果继续在index边上变换就又变换回去了，那么就会有几个递归变换过程反复进行重复的变换,没有必要）的7条边上进行递归变换
            if(i!=index){
                Transform(i,times);
            }
        }
        edges[index]=!edges[index];     //使用回溯法还原到没有变换前的状态，使用回溯法因为只有一个edges数组存储状态，所以我们每次变换寻找最短次数后都得还原，不影响其他其他情形下的递归和过程和查找
        edges[(index+1)%8]=!edges[(index+1)%8];
        edges[(index+7)%8]=!edges[(index+7)%8];
        times--;
    }
}
