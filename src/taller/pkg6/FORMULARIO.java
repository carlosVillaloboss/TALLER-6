/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package taller.pkg6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FORMULARIO extends JFrame implements ActionListener {
    JTextField nameField, numberField;
    JTextArea displayArea;
    JButton createBtn, readBtn, updateBtn, deleteBtn;

    public FORMULARIO() {
        setTitle("Gestor de Contactos");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        nameField = new JTextField(15);
        numberField = new JTextField(15);
        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);

        createBtn = new JButton("Crear");
        readBtn = new JButton("Leer");
        updateBtn = new JButton("Actualizar");
        deleteBtn = new JButton("Eliminar");

        createBtn.addActionListener(this);
        readBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);

        add(new JLabel("Nombre:"));
        add(nameField);
        add(new JLabel("Número:"));
        add(numberField);
        add(createBtn);
        add(readBtn);
        add(updateBtn);
        add(deleteBtn);
        add(new JScrollPane(displayArea));

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createBtn) {
            createContact(nameField.getText(), numberField.getText());
        } else if (e.getSource() == readBtn) {
            readContacts();
        } else if (e.getSource() == updateBtn) {
            updateContact(nameField.getText(), numberField.getText());
        } else if (e.getSource() == deleteBtn) {
            deleteContact(nameField.getText());
        }
    }

    public void createContact(String name, String number) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("friendsContact.txt", true))) {
            writer.write(name + "," + number + "\n");
            displayArea.setText("Contacto creado: " + name);
        } catch (IOException e) {
            displayArea.setText("Error al crear el contacto");
        }
    }

    public void readContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("friendsContact.txt"))) {
            String line;
            displayArea.setText("");
            while ((line = reader.readLine()) != null) {
                displayArea.append(line + "\n");
            }
        } catch (IOException e) {
            displayArea.setText("Error al leer los contactos");
        }
    }

    public void updateContact(String name, String newNumber) {
        File tempFile = new File("temp.txt");
        File originalFile = new File("friendsContact.txt");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(name)) {
                    writer.write(name + "," + newNumber + "\n");
                    found = true;
                } else {
                    writer.write(line + "\n");
                }
            }

            if (found) {
                displayArea.setText("Contacto actualizado: " + name);
            } else {
                displayArea.setText("Contacto no encontrado: " + name);
            }

        } catch (IOException e) {
            displayArea.setText("Error al actualizar el contacto");
        }

        if (originalFile.delete()) {
            tempFile.renameTo(originalFile);
        }
    }

    public void deleteContact(String name) {
        File tempFile = new File("temp.txt");
        File originalFile = new File("friendsContact.txt");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (!details[0].equals(name)) {
                    writer.write(line + "\n");
                } else {
                    found = true;
                }
            }

            if (found) {
                displayArea.setText("Contacto eliminado: " + name);
            } else {
                displayArea.setText("Contacto no encontrado: " + name);
            }

        } catch (IOException e) {
            displayArea.setText("Error al eliminar el contacto");
        }

        if (originalFile.delete()) {
            tempFile.renameTo(originalFile);
        }
    }

    public static void main(String[] args) {
        new FORMULARIO();
    }
}
