#include <stdio.h>
#include <limits.h>
#include <stdbool.h>

#define N 6 // Número de nós
#define INF INT_MAX

void printDistances(int distances[], int previous[], int startNode) {
    printf("\nMenor custo a partir do no %d:\n", startNode);
    for (int i = 0; i < N; i++) {
        if (distances[i] == INF) {
            printf("No %d: INF\n", i);
        } else {
            printf("No %d: %d (via no %d)\n", i, distances[i], previous[i]);
        }
    }
}

int findMinDistanceNode(bool visited[], int distances[]) {
    int minDistance = INF;
    int minIndex = -1;

    for (int i = 0; i < N; i++) {
        if (!visited[i] && distances[i] < minDistance) {
            minDistance = distances[i];
            minIndex = i;
        }
    }

    return minIndex;
}

void dijkstra(int graph[N][N], int startNode) {
    int distances[N];   // Armazena as menores distâncias
    bool visited[N];    // Marca os nós já processados
    int previous[N];    // Para rastrear o caminho mais curto

    // Inicialização
    for (int i = 0; i < N; i++) {
        distances[i] = INF;
        visited[i] = false;
        previous[i] = -1;
    }

    distances[startNode] = 0; // Distância para si mesmo é 0

    for (int i = 0; i < N; i++) {
        int currentNode = findMinDistanceNode(visited, distances);
        if (currentNode == -1) break; // Nenhum nó alcançável restante

        visited[currentNode] = true;

        // Atualiza as distâncias dos vizinhos
        for (int neighbor = 0; neighbor < N; neighbor++) {
            if (!visited[neighbor] && graph[currentNode][neighbor] != 0 && graph[currentNode][neighbor] != INF) {
                int newDistance = distances[currentNode] + graph[currentNode][neighbor];
                if (newDistance < distances[neighbor]) {
                    distances[neighbor] = newDistance;
                    previous[neighbor] = currentNode;
                }
            }
        }
    }

    printDistances(distances, previous, startNode);
}

void simulateChanges(int graph[N][N]) {
    printf("\n** POC: Alteracao de custo **\n");
    printf("Alterando o custo entre os nos 1 e 4 para 2...\n");
    graph[1][4] = 2;
    graph[4][1] = 2;
    dijkstra(graph, 0);

    printf("\n** POC: Remocao de no **\n");
    printf("Removendo o no 2...\n");
    for (int i = 0; i < N; i++) {
        graph[2][i] = INF;
        graph[i][2] = INF;
    }
    dijkstra(graph, 0);

    printf("\n** POC: Adicao de no **\n");
    printf("Adicionando o no 2 novamente com custos restaurados...\n");
    graph[2][3] = 3;
    graph[3][2] = 3;
    dijkstra(graph, 0);
}

int main() {
    // Matriz de adjacência representando o grafo
    int graph[N][N] = {
        {0, 4, INF, INF, INF, INF},
        {4, 0, 8, INF, INF, INF},
        {INF, 8, 0, 7, INF, 4},
        {INF, INF, 7, 0, 9, 14},
        {INF, INF, INF, 9, 0, 10},
        {INF, INF, 4, 14, 10, 0}
    };

    printf("** Simulacao inicial **\n");
    dijkstra(graph, 0);

    simulateChanges(graph);

    return 0;
}
