/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto (ProdutosDTO produto){
        conn = new conectaDAO().connectDB(); // Certifique-se de que esta classe conecta corretamente ao banco
        
        String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
        
        try {
            prep = conn.prepareStatement(sql);
            prep.setString(1, produto.getNome()); 
            prep.setDouble(2, produto.getValor());   
            prep.setString(3, produto.getStatus()); 

            prep.executeUpdate(); // Executa o comando de inserção
            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + e.getMessage());
        } finally {
            try {
                if (prep != null) prep.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
     // Método para vender um produto, atualizando seu status para "Vendido"
    public boolean venderProduto(int idProduto) {
        String sql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProduto);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0; // Retorna true se a atualização foi bem-sucedida
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<ProdutosDTO> listarProdutos(){
        ArrayList<ProdutosDTO> listaProdutos = new ArrayList<>();
        
        // Conectando ao banco de dados
        Connection conn = new conectaDAO().connectDB();
        
        String sql = "SELECT * FROM produtos"; // A consulta para listar todos os produtos
        
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(); // Executa a consulta
            
            // Enquanto houver produtos na tabela, ele os adiciona à lista
            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                
                // Preenche o DTO com os dados do sproduto
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getDouble("valor"));
                produto.setStatus(rs.getString("status"));
                
                // Adiciona o produto à lista
                listaProdutos.add(produto);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Caso haja erro, imprime a mensagem
        } finally {
            try {
                conn.close(); // Fecha a conexão
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return listaProdutos; // Retorna a lista de produtos
    }
    
    // Método para listar produtos vendidos
    public ArrayList<ProdutosDTO> listarProdutosVendidos() {
        ArrayList<ProdutosDTO> produtosVendidos = new ArrayList<>();
        
        String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";  // Query para buscar produtos vendidos

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getDouble("valor"));
                produto.setStatus(rs.getString("status"));
                
                // Adiciona o produto à lista
                produtosVendidos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produtosVendidos;
    }
    
    // Método para vender um produto, atualizando o status para "Vendido"     
}