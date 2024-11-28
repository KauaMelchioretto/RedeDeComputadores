#include <stdio.h>
#include <string.h>

#define N 5 // Numero de nos
#define INF -1 // Representacao de caminho vazio

typedef struct {
    int destino;
    int via;
} Caminho;

void printCaminhos(Caminho caminhos[N], int meuId) {
    printf("\nTabela de caminhos do no %d:\n", meuId);
    for (int i = 0; i < N; i++) {
        if (caminhos[i].via == INF) {
            printf("Destino %d: Caminho vazio\n", i);
        } else if (caminhos[i].via == meuId) {
            printf("Destino %d: Eu mesmo\n", i);
        } else {
            printf("Destino %d: Via no %d\n", i, caminhos[i].via);
        }
    }
}

void inicializarCaminhos(Caminho caminhos[N], int grafo[N][N], int meuId) {
    for (int i = 0; i < N; i++) {
        if (i == meuId) {
            caminhos[i].destino = i;
            caminhos[i].via = meuId;
        } else if (grafo[meuId][i] != INF) {
            caminhos[i].destino = i;
            caminhos[i].via = i;
        } else {
            caminhos[i].destino = i;
            caminhos[i].via = INF;
        }
    }
}

void atualizarCaminhos(Caminho caminhos[N], Caminho caminhosVizinho[N], int meuId, int vizinhoId) {
    for (int i = 0; i < N; i++) {
        if (caminhosVizinho[i].via == meuId) {
            // Evitar loops
            continue;
        }

        if (caminhos[i].via == INF || caminhosVizinho[i].via != INF) {
            caminhos[i].via = vizinhoId;
        }
    }
}

void enviarCaminhos(Caminho caminhos[N], Caminho caminhosRecebidos[N], int destino) {
    memcpy(caminhosRecebidos, caminhos, sizeof(Caminho) * N);
}

void simulateChanges(int grafo[N][N], Caminho caminhos[N]) {
    printf("\n** POC: Alteracao de conexoes **\n");
    printf("Adicionando um caminho entre os nos 3 e 4...\n");
    grafo[3][4] = 1;
    grafo[4][3] = 1;
    inicializarCaminhos(caminhos, grafo, 0);
    printCaminhos(caminhos, 0);

    printf("\nRemovendo a conexao entre os nos 1 e 2...\n");
    grafo[1][2] = INF;
    grafo[2][1] = INF;
    inicializarCaminhos(caminhos, grafo, 0);
    printCaminhos(caminhos, 0);
}

int main() {
    // Matriz de adjacencia do grafo
    int grafo[N][N] = {
        { 0, 1, INF, INF, INF },
        { 1, 0, 1, INF, INF },
        { INF, 1, 0, 1, INF },
        { INF, INF, 1, 0, 1 },
        { INF, INF, INF, 1, 0 }
    };

    Caminho caminhos[N];
    Caminho caminhosRecebidos[N];

    int meuId = 0;

    // Inicializacao
    inicializarCaminhos(caminhos, grafo, meuId);
    printCaminhos(caminhos, meuId);

    // Simulacao da POC
    simulateChanges(grafo, caminhos);

    return 0;
}
