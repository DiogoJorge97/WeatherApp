# WeatherApp

Aplicação de meteorologia que recebe dados da API do IPMA e os guarda numa base de dados local

## Explicação sucinta da funcionalidade implementada

Através desta aplicação o utilizador pode ter acesso à meteorologia de uma determinada cidade num periodo de 5 dias. É permitido ao utilizador ver dados como temperaturas, probabilidade de percepitação e direção/velocidade do vento.
O utilizador pode a qualquer momento pesquisar por outra cidade e irá ser feito o display dos dados associados a essa cidade.
O recarregamento de dados é feito apenas para as cidades pesquisadas de forma a diminuir o consumo de tráfego de dados.
Sempre que o utilizador entra na aplicação e tiver acesso à Internet sáo actualizados os dados, caso não exista ligação será feito o display dos dados guardados em cache, assim que o utilizador voltar a ter acesso à internet apenas precisa de carregar no botão de ```Refresh``` a página e os dados serão atualizados

## Explicação geral da arquitetura seguida

A arquitetura seguida foi sugerida nos requesitos do  [trabalho](https://prnt.sc/m395wa).

No BackEnd da app existe um Repositório que controla e gere os dados que serão inseridos na UI Thread, este repositório é que decide que dados irá transmitir ao ViewModel, tendo assim duas possibilidades
* Retrofit para aceder à API do IPMA
* Room para aceder à base de dados e dar assim uma camada de abstração sobre o SQLite

O FrontEnd é "gerido" pelo ViewModel que usa LiveData de forma a poder observar se existem alterações que necessitem de ser apresentadas na Interface (MainActivity)

## Limitações

A aplicação não faz o update automatico de x em x tempo, não foi implementada essa funcionalidade.
Design um pouco pobre mas suficiente para as funcionalidades implementadas.


## Cuidados particulares para colocar o projeto a correr

Não considore que precise de cuidados ao correr o projeto, como é de esperar se ao iniciar a app pela primeira vez o dispositivo não tiver acesso à internet não serão apresentados dados, para resolver esta situação basta retomar o acesso à Internet e carregar no botão ```Refresh```

### Anexo
* [ScreanShot](https://prnt.sc/m394xv) - MainActivity
