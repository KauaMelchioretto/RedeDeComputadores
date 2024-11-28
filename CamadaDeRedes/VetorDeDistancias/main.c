#include <stdio.h>
#include <limits.h>

#define N 6
#define INF INT_MAX

void printDistances(int distances[N]) {
    printf("Vetor de Distancias: ");
    for (int i = 0; i < N; i++) {
        if (distances[i] == INF) {
            printf("INF ");
        } else {
            printf("%d ", distances[i]);
        }
    }
    printf("\n");
}

void distanceVectorRouting(int cost[N][N], int startNode) {
    int distances[N];
    int updated;

    // Inicializa as distâncias
    for (int i = 0; i < N; i++) {
        distances[i] = (cost[startNode][i] != 0) ? cost[startNode][i] : INF;
    }
    distances[startNode] = 0; // Distância para si mesmo é 0

    printf("Inicializacao para o no %d:\n", startNode);
    printDistances(distances);

    // Atualização iterativa usando a equação de Bellman-Ford
    do {
        updated = 0; // Indica se houve mudanças
        for (int i = 0; i < N; i++) {
            if (i == startNode || distances[i] == INF) continue;

            for (int j = 0; j < N; j++) {
                if (cost[i][j] != 0 && cost[i][j] != INF && distances[i] + cost[i][j] < distances[j]) {
                    distances[j] = distances[i] + cost[i][j];
                    updated = 1;
                }
            }
        }

        printf("Apos uma atualizacao:\n");
        printDistances(distances);

    } while (updated);

    printf("Distancias finais do no %d:\n", startNode);
    printDistances(distances);
}

void simulateChanges(int cost[N][N]) {
    printf("\n** POC: Mudanca no custo do enlace **\n");
    printf("Alterando custo entre os nos 1 e 4 para 10...\n");
    cost[1][4] = 10;
    cost[4][1] = 10;

    distanceVectorRouting(cost, 1);

    printf("\n** POC: Remocao de um no **\n");
    printf("Removendo o no 3 (tornando os custos infinitos)...\n");
    for (int i = 0; i < N; i++) {
        cost[3][i] = INF;
        cost[i][3] = INF;
    }

    distanceVectorRouting(cost, 1);

    printf("\n** POC: Adicao de um novo no **\n");
    printf("Simulando adicao do no 3 novamente com custos restaurados...\n");
    cost[3][2] = 5;
    cost[2][3] = 5;
    cost[3][4] = 7;
    cost[4][3] = 7;

    distanceVectorRouting(cost, 1);
}

int main() {
    // Matriz de custos (0 indica que não há conexão)
    int cost[N][N] = {
        {0, 2, 0, 1, 0, 0},
        {2, 0, 3, 2, 0, 0},
        {0, 3, 0, 0, 7, 0},
        {1, 2, 0, 0, 3, 6},
        {0, 0, 7, 3, 0, 1},
        {0, 0, 0, 6, 1, 0}
    };

    printf("Simulacao inicial\n");
    distanceVectorRouting(cost, 0);

    simulateChanges(cost);

    return 0;
}
