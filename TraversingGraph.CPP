#include <iostream>
#include <malloc.h>
using namespace std;
#define NULL 0
#define MAX_VERTEX_NUM 30  //最大顶点数
typedef struct ArcNode{
	int adjvex;   //该边所指的顶点的位置
	struct ArcNode *nextarc;   //指向下一条边的指针
}ArcNode;   //表的结点

typedef struct VNode{
	char data;        //顶点信息（如数据等）
	ArcNode *firstarc;        //指向第一条依附该顶点的边的弧指针
}VNode, AdjList[MAX_VERTEX_NUM];   //头结点

typedef struct MGraph{
	AdjList vertices;
	int visited[MAX_VERTEX_NUM];    //访问标志数组
	int vexnum, arcnum;   //图的当前顶点数和弧数
}MGraph;

//返回顶点v在顶点向量中的位置
int LocateVex(MGraph &G, char v){
	int i;
	for (i = 0; v != G.vertices[i].data && i < G.vexnum; i++)
		;
	return i;
}

//输入顶点
void add_vex(MGraph &G){
	cout << "输入无向图顶点数: " << endl;
	cin >> G.vexnum;
	for (int i = 0; i < G.vexnum; i++){
		cout << "输入第" << i + 1 << "个顶点:" << endl;
		cin >> G.vertices[i].data;   //构造顶点向量
		G.vertices[i].firstarc = NULL;
	}
}

//输入边
void add_arc(MGraph &G){
	ArcNode *s, *t, *p;
	cout << "输入无向图边数: " << endl;
	cin >> G.arcnum;
	char v1, v2;
	for (int k = 0; k < G.arcnum; k++){
		cout << "输入第" << k + 1 << "条边关联的两个顶点:" << endl;
		cin >> v1 >> v2;
		int i = LocateVex(G, v1);
		int j = LocateVex(G, v2);    //确定v1 , v2在G中的位置
		s = (ArcNode*)malloc(sizeof(ArcNode));
		t = (ArcNode*)malloc(sizeof(ArcNode));
		s->adjvex = j;   //该边所指向的顶点的位置为j
		s->nextarc = NULL;
		if (!G.vertices[i].firstarc){
			G.vertices[i].firstarc = s;
		}
		else{
			for (p = G.vertices[i].firstarc; p->nextarc; p = p->nextarc)
				;
			p->nextarc = s;
		}
		t->adjvex = i;   //该边所指向的顶点的位置为j
		t->nextarc = NULL;
		if (!G.vertices[j].firstarc){
			G.vertices[j].firstarc = t;
		}
		else{
			for (p = G.vertices[j].firstarc; p->nextarc; p = p->nextarc)
				;
			p->nextarc = t;
		}
	}
}

//构造邻接表
MGraph CreateMGraph(MGraph &G){
	add_vex(G);        //增加节点
	add_arc(G);        //增加边     
	cout << "无向图创建成功！" << endl;
	return G;
}

//输出邻接表
void PrintMGraph(MGraph &G){
	int i;
	ArcNode *p;
	cout << "编号    顶点    邻点编号" << endl;
	for (i = 0; i < G.vexnum; i++){
		cout << "  " << i << "       " << G.vertices[i].data << "      ";
		for (p = G.vertices[i].firstarc; p; p = p->nextarc)
			cout << p->adjvex << "  ";
		cout << endl;
	}
}


//(深度优先搜索)
void DFS(MGraph &G, int i, int s, char path[]){
	ArcNode *p;
	G.visited[i] = 1;
	static int found = 0;
	static int rear = 0;        //路径数组的尾指针
	path[rear++] = G.vertices[i].data;
	path[rear] = '\0';    //注意将最后一位设为'\0'
	for (p = G.vertices[i].firstarc; p && !found; p = p->nextarc)
	if (s == p->adjvex){    //找到目标节点
		found = 1;    //found置为1，退出
		path[rear++] = G.vertices[p->adjvex].data;
		path[rear] = '\0';
	}
	else if (!G.visited[p->adjvex]){
		DFS(G, p->adjvex, s, path);
	}
	if (!found)    //如果该节点的所有邻接点都不是目标节点，并且邻接点的邻接点也不是目标节点，那么它就不是路径上的。退出路径
		path[--rear] = '\0';
}

typedef struct qnode{
	char data;
	struct qnode * pre;
	struct qnode * next;
}Qnode, *Queueptr;    //    创建链 Qnode是struct qnode的别名，Queueptr是struct qnode *的别名

typedef struct{
	Queueptr front;    //队头指针
	Queueptr rear;    //队尾指针
}LinkQueue;    //创建队列

//初始化队列
void InitQueue(LinkQueue *Q){
	Q->front = (Queueptr)malloc(sizeof(Qnode));    //队头和队尾指向头结点
	if (!Q->front){
		cout << "无可用内存！" << endl;    //存储分配失败
	}
	else{
		Q->front->next = NULL;
		Q->rear = Q->front;
	}
}

//入队函数
void Enqueue(LinkQueue *Q, int value){
	Queueptr newp = (Queueptr)malloc(sizeof(Qnode));
	if (!newp)
		cout << "无可用内存！" << endl;    //存储分配失败
	newp->data = value;
	newp->next = NULL;
	Q->rear->next = newp;    //p插入原队尾
	Q->rear = newp;    //p成为新的队尾
}

//(广度优先搜索)
void BFS(MGraph &G, int i, int s, char path[]){    //从第i个顶点开始搜索
	ArcNode *p;
	int found = 0;
	char gettop;
	int top;
	for (int x = 0; x<G.vexnum; x++)
		G.visited[x] = 0;
	LinkQueue *Q = (LinkQueue *)malloc(sizeof(LinkQueue));
	InitQueue(Q);			//初始化队列
	G.visited[i] = 1;        //处理源点
	Enqueue(Q, G.vertices[i].data);    //源点入队
	Q->rear->pre = Q->front;    //第一个节点的前驱指针指向front
	Queueptr temp = Q->front->next;        //处理队列中的每个顶点
	while (!found){
		gettop = temp->data;
		top = LocateVex(G, gettop);
		for (p = G.vertices[top].firstarc; p && !found; p = p->nextarc){
			if (s == p->adjvex){    //找到目标点
				found = 1;    //标志设为1，入队列，退出
				Enqueue(Q, G.vertices[p->adjvex].data);
				Q->rear->pre = temp;    //加入队列中的点，前驱指针指向它的源点
			}
			else if (!G.visited[p->adjvex]){    //如果不是，就访问，并加入队列
				G.visited[p->adjvex] = 1;
				Enqueue(Q, G.vertices[p->adjvex].data);    //入队
				Q->rear->pre = temp;
			}
		}
		temp = temp->next;
	}//while
	int j = 0;
	for (temp = Q->rear; temp != Q->front; temp = temp->pre)    //复制队列中的顶点到字符串中去
		path[j++] = temp->data;
	path[j] = '\0';
	free(Q);
}

//求图的最短路径
void GetV(MGraph &G, char way){
	char start, end;
	cout << "请选择一个开始顶点：";
	cin >> start;
	while (LocateVex(G, start) >= G.vexnum){
		cout << "没有找到此开始顶点！请重新输入：";
		cin >> start;
	}
	cout << "请选择一个结束顶点：";
	cin >> end;
	while (LocateVex(G, end) >= G.vexnum){
		cout << "没有找到此结束顶点！请重新选择一个顶点：";
		cin >> end;
	}
	int i = LocateVex(G, start);
	int s = LocateVex(G, end);
	int j = 0, x = 0;
	for (j = 0; j<G.vexnum; j++)
		G.visited[j] = 0;
	if (way == 'D'){
		char *path;        //存放路径
		path = (char *)malloc(MAX_VERTEX_NUM*sizeof(char));
		DFS(G, i, s, path);
		cout << "顶点" << start << "到顶点" << end << "的简单路径为:" << endl;
		while (path[x])
			cout << path[x++];    //输出路径
	}
	else{
		char *path;        //存放路径
		path = (char *)malloc(MAX_VERTEX_NUM*sizeof(char));
		BFS(G, s, i, path);    //广度优先搜索
		cout << "顶点" << start << "到顶点" << end << "的最短路径为:" << endl;
		while (path[x]){
			cout << path[x++];    //输出路径
		}
		cout << endl;
		cout << "顶点" << start << "到顶点" << end << "的最短路径长度为:" << x-1 << endl;
	}
	cout << endl;
}

void display_menu(){
	int i;
	char ch = 'y';
	char way;
	MGraph G;
	G.vexnum = 0;
	while (ch == 'y'){
		cout << "--------------------菜单-------------------" << endl;
		cout << " " << "1  |利用邻接表创建图" << endl;
		cout << " " << "2  |显示图的邻接表" << endl;
		cout << " " << "3  |求一条从顶点 i 到顶点 s 的简单路径" << endl;
		cout << " " << "4  |求两个顶点之间的一条路径长度最短的路径" << endl;
		cout << " " << "0  |退出！" << endl;
		cout << "---------------------------------------------" << endl;
		cout << endl << "请选择要进行的操作(0~4)：";
		cin >> i;
		switch (i){
		case 1:
			if (G.vexnum == 0)
				CreateMGraph(G);
			else
				cout << "图已创建！" << endl;
			break;
		case 2:
			if (G.vexnum != 0)
				PrintMGraph(G);
			else
				cout << "图为空！" << endl;
			break;
		case 3:
			if (G.vexnum == 0)
				cout << "请先创建图！" << endl;
			else{
				way = 'D';
				GetV(G, way);
			}
			break;
		case 4:
			if (G.vexnum == 0)
				cout << "请先创建图！" << endl;
			else{
				way = 'B';
				GetV(G, way);
			}
			break;
		case 0:
			exit(1);
		default:
			cout << "请在0~4之间进行选择！" << endl;
		}
		cout << "是否继续y/n：";
		cin >> ch;
		system("cls");
	}
}
int main()
{
display_menu();
return 0;
}
