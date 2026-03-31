import java.util.*;

// ================= CLIENTE =================
class Cliente {
    int id;
    String nome;

    public Cliente(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}

// ================= PRODUTO =================
class Produto {
    int id;
    String nome;
    double preco;

    public Produto(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public String toString() {
        return nome + " - R$" + preco;
    }
}

// ================= ITEM DO PEDIDO =================
class ItemPedido {
    Produto produto;
    int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return produto.preco * quantidade;
    }
}

// ================= PEDIDO =================
class Pedido {
    int id;
    Cliente cliente;
    List<ItemPedido> itens = new ArrayList<>();

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        itens.add(new ItemPedido(produto, quantidade));
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void mostrarPedido() {
        System.out.println("\nResumo do pedido:");
        for (ItemPedido item : itens) {
            System.out.println(item.produto.nome + " | Quantidade: " + item.quantidade);
        }
        System.out.println("Total: R$" + calcularTotal());
    }
}

// ================= PAGAMENTO (SINGLETON) =================
class PagamentoService {

    private static PagamentoService instancia;

    private PagamentoService() {}

    public static PagamentoService getInstancia() {
        if (instancia == null) {
            instancia = new PagamentoService();
        }
        return instancia;
    }

    public boolean processarPagamento(double valor) {

        System.out.println("\nConectando ao sistema de pagamento...");
        System.out.println("Processando pagamento de R$" + valor + "...");

        // NOVO: teto de gasto = 6500
        if (valor <= 6500) {
            System.out.println("Pagamento aprovado.");
            return true;
        } else {
            System.out.println("Pagamento recusado (valor acima do limite permitido).");
            return false;
        }
    }
}

// ================= SISTEMA =================
class SistemaLoja {
    List<Produto> produtos = new ArrayList<>();

    public void adicionarProduto(Produto p) {
        produtos.add(p);
    }

    public void listarProdutos() {
        System.out.println("\nProdutos disponíveis:");
        for (Produto p : produtos) {
            System.out.println(p.id + " - " + p);
        }
    }

    public Produto buscarProduto(int id) {
        for (Produto p : produtos) {
            if (p.id == id) return p;
        }
        return null;
    }
}

// ================= MAIN =================
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Cliente cliente = new Cliente(1, "Lojinha");

        SistemaLoja sistema = new SistemaLoja();

        // PRODUTOS ATUALIZADOS
        sistema.adicionarProduto(new Produto(1, "Notebook", 3000));
        sistema.adicionarProduto(new Produto(2, "Mouse Gamer", 150));
        sistema.adicionarProduto(new Produto(3, "Teclado Mecânico", 250));
        sistema.adicionarProduto(new Produto(4, "Mouse Pad", 100));
        sistema.adicionarProduto(new Produto(5, "Monitor", 1000));
        sistema.adicionarProduto(new Produto(6, "Fone de Ouvido", 120));
        sistema.adicionarProduto(new Produto(7, "Cadeira de Escritório", 400));

        Pedido pedido = new Pedido(1, cliente);

        System.out.println("Bem-vindo à Lojinha Online");
        System.out.println("Cliente: " + cliente.nome);

        // SELEÇÃO DE PRODUTOS
        while (true) {

            sistema.listarProdutos();

            System.out.print("\nDigite o ID do produto: ");
            int id = sc.nextInt();

            Produto produto = sistema.buscarProduto(id);

            if (produto == null) {
                System.out.println("Produto não encontrado.");
                continue;
            }

            System.out.print("Digite a quantidade: ");
            int qtd = sc.nextInt();

            pedido.adicionarItem(produto, qtd);

            System.out.print("Deseja adicionar mais produtos? (s/n): ");
            String continuar = sc.next();

            if (!continuar.equalsIgnoreCase("s")) {
                break;
            }
        }

        // REVISÃO
        pedido.mostrarPedido();

        System.out.print("\nDeseja confirmar a compra? (s/n): ");
        String resposta = sc.next();

        if (!resposta.equalsIgnoreCase("s")) {
            System.out.println("Compra cancelada.");
            return;
        }

        // PAGAMENTO
        PagamentoService pagamento = PagamentoService.getInstancia();
        boolean sucesso = pagamento.processarPagamento(pedido.calcularTotal());

        if (!sucesso) {
            System.out.print("\nPagamento falhou. Deseja tentar novamente? (s/n): ");
            String tentar = sc.next();

            if (tentar.equalsIgnoreCase("s")) {
                sucesso = pagamento.processarPagamento(pedido.calcularTotal());
            } else {
                System.out.println("Processo encerrado.");
                return;
            }
        }

        // FINALIZAÇÃO
        if (sucesso) {
            System.out.println("\nPedido finalizado com sucesso.");
            System.out.println("Resumo exibido ao cliente.");
            return;
        }
    }
}