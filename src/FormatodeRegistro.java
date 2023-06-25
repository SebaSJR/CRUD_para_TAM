import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class FormatodeRegistro extends JDialog {
    private JTextField tfNombre;
    private JTextField tfEmail;
    private JTextField tfTelefono;
    private JTextField tfDireccion;
    private JPasswordField pfContraseña;
    private JPasswordField pfConfirmarContraseña;
    private JButton btnRegistrarse;
    private JButton btnCancelar;
    private JPanel paneldeRegistro;
    private JButton btnEliminar;
    private JButton btnActualizar;
    private JButton btnRead;
    private   Usuario user;
    public FormatodeRegistro(JFrame parent) {
        super(parent);
        setTitle("Ejercicio CRUD");
        setContentPane(paneldeRegistro);
        setMinimumSize(new Dimension(600, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnRegistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createUser();
            }
        });
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { deleteUser();

            }
        });


        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { updateUser();

            }
        });
        setVisible(true);
    }

    private void createUser() {
        String nombre = tfNombre.getText();
        String email = tfEmail.getText();
        String telefono = tfTelefono.getText();
        String direccion = tfDireccion.getText();
        String contraseña = String.copyValueOf(pfContraseña.getPassword());
        String confirmarContraseña = String.valueOf(pfConfirmarContraseña.getPassword());

        if (nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor llenar los campos",
                    "Inente nuevamente",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!contraseña.equals(confirmarContraseña)){
            JOptionPane.showMessageDialog(this,
                    "Confirmar contraseña no coincide",
                    "Intete nuevamete",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(nombre, email, telefono, direccion, contraseña);
        if (user != null) {
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Fallo en registrar nuevo usuario",
                    "intente de nuevo",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Usuario addUserToDatabase(String nombre, String email, String telefono, String direccion, String contraseña) {
        Usuario user = null;
        final String DB_URL = "jdbc:mysql://localhost:4306/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //conexion a base de datos exitosamente...

            Statement stnt = conn.createStatement();
            String sql = "INSERT INTO mystore (Nombre, Email, Telefono, direccion, Contraseña) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, telefono);
            preparedStatement.setString(4, direccion);
            preparedStatement.setString(5, contraseña);

            //Insertar fila en la tabla
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new Usuario();
                user.nombre = nombre;
                user.email = email;
                user.telefono = telefono;
                user.direccion = direccion;
                user.contraseña = contraseña;
            }

            stnt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args){
        FormatodeRegistro myForm = new FormatodeRegistro(null);
        Usuario user = myForm.user;
        if (user != null) {
            System.out.println("Registro exitoso de: " + user.nombre);
        }
        else {
            System.out.println("Registro cancelado");
        }
    }
    private void deleteUser() {
        String telefono = tfTelefono.getText();

        user = deleteUserToDatabase(telefono);
        if (user != null) {
                System.out.println("Registro eliminado: " + user.telefono);

            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Fallo en eliminar usuario",
                    "intente de nuevo",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private Usuario deleteUserToDatabase(String telefono) {
        Usuario user = null;
        final String DB_URL = "jdbc:mysql://localhost:4306/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //conexion a base de datos exitosamente...

            Statement stnt = conn.createStatement();
            String sql = "DELETE FROM mystore WHERE telefono = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, telefono);

            //eliminar el registro en la tabla
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new Usuario();
                user.telefono = telefono;

            }

            stnt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    private void updateUser() {
        String nombre = tfNombre.getText();
        String email = tfEmail.getText();
        String telefono = tfTelefono.getText();
        String direccion = tfDireccion.getText();
        String contraseña = String.copyValueOf(pfContraseña.getPassword());
        //String confirmarContraseña = String.valueOf(pfConfirmarContraseña.getPassword());


        user = updateUserToDatabase(nombre, email, telefono, direccion, contraseña);
        if (user != null) {
            dispose();
        }

    }

    private Usuario updateUserToDatabase(String nombre, String email, String telefono, String direccion, String contraseña) {
        Usuario user = null;
        final String DB_URL = "jdbc:mysql://localhost:4306/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //conexion a base de datos exitosamente...

            Statement stnt = conn.createStatement();
            String sql = "UPDATE mystore SET Nombre = ?, Email = ?, direccion = ?, Contraseña = ? WHERE Telefono = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, direccion);
            preparedStatement.setString(4, contraseña);
            preparedStatement.setString(5, telefono);

            //Modificar fila en la tabla
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new Usuario();
                user.nombre = nombre;
                user.email = email;
                user.telefono = telefono;
                user.direccion = direccion;
                user.contraseña = contraseña;
            }

            stnt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

}
