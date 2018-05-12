package javrsim.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import javrsim.windows.SimulationWindow.DataCellRenderer;

public class DataViewWindow {

	public JPanel constructDataPanel() {
		TableModel dataModel = new AbstractTableModel() {
			@Override
			public int getColumnCount() {
				return 17;
			}

			@Override
			public int getRowCount() {
				return mcu.getData().size() / 16;
			}

			@Override
			public String getValueAt(int row, int col) {
				if(col == 0) {
					return String.format("%04X",row*16);
				} else {
					int address = (row * 16) + (col-1);
					return String.format("%02X",mcu.getData().peek(address));
				}
			}
		};
		JTable table = new JTable(dataModel);
		// Configure Table Headings
		table.getColumnModel().getColumn(0).setHeaderValue("Address" );
		table.getColumnModel().getColumn(0).setCellRenderer(new CodeCellRenderer(VERY_LIGHT_GRAY,Color.WHITE,LIGHT_RED,FONT_BOLD));
		for(int i=0;i!=16;++i) {
			table.getColumnModel().getColumn(i+1).setHeaderValue(Integer.toHexString(i));
			table.getColumnModel().getColumn(i + 1).setCellRenderer(new DataCellRenderer(Color.GRAY, Color.DARK_GRAY,
					LIGHT_GREEN, LIGHT_RED, LIGHT_YELLOW, FONT_PLAIN));
		}
		//
		return createTablePanel(table);
	}

	private class DataCellRenderer extends DefaultTableCellRenderer {
		private final Color first;
		private final Color second;
		private final Color read;
		private final Color write;
		private final Color access;
		private final Font font;

		public DataCellRenderer(Color first, Color second, Color read, Color write, Color access, Font font) {
			this.first = first;
			this.second = second;
			this.read = read;
			this.write = write;
			this.access = access;
			this.font = font;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			int address = (row * 16) + (column - 1);
			if (mcu.wasRead(address)) {
				c.setBackground(read);
			} else if (mcu.wasWritten(address)) {
				c.setBackground(write);
			} else if (mcu.wasAccessedEver(address)) {
				c.setBackground(access);
			} else if (row % 2 == 0) {
				c.setBackground(first);
			} else {
				c.setBackground(second);
			}
			c.setFont(font);
			return c;
		}
	}


	public JPanel createTablePanel(JTable table) {
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		Border cb = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3),
				BorderFactory.createLineBorder(Color.gray));
		centerPanel.setBorder(cb);
		centerPanel.add(scrollPane, BorderLayout.EAST);
		return centerPanel;
	}

}