import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS } from '../constants/theme';

export default function FolderPicker({ folders, selectedFolder, onSelectFolder, disabled }) {
  return (
    <View style={styles.container}>
      <Text style={styles.label}>Folder</Text>
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={styles.scrollContent}
      >
        <TouchableOpacity
          style={[
            styles.folderOption,
            selectedFolder === null && styles.selectedFolder,
            disabled && styles.disabled,
          ]}
          onPress={() => !disabled && onSelectFolder(null)}
          disabled={disabled}
        >
          <Ionicons
            name="remove"
            size={18}
            color={selectedFolder === null ? '#fff' : COLORS.textSecondary}
          />
          <Text
            style={[
              styles.folderText,
              selectedFolder === null && styles.selectedFolderText,
            ]}
          >
            None
          </Text>
        </TouchableOpacity>

        {folders.map((folder) => (
          <TouchableOpacity
            key={folder.id}
            style={[
              styles.folderOption,
              { borderColor: folder.color },
              selectedFolder === folder.id && { backgroundColor: folder.color },
              disabled && styles.disabled,
            ]}
            onPress={() => !disabled && onSelectFolder(folder.id)}
            disabled={disabled}
          >
            <Ionicons
              name={folder.icon}
              size={18}
              color={selectedFolder === folder.id ? '#fff' : folder.color}
            />
            <Text
              style={[
                styles.folderText,
                selectedFolder === folder.id && styles.selectedFolderText,
              ]}
            >
              {folder.name}
            </Text>
          </TouchableOpacity>
        ))}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: 12,
  },
  scrollContent: {
    gap: 10,
  },
  folderOption: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingVertical: 10,
    borderRadius: 20,
    borderWidth: 2,
    borderColor: COLORS.border,
    gap: 8,
  },
  selectedFolder: {
    backgroundColor: COLORS.primary,
    borderColor: COLORS.primary,
  },
  selectedFolderText: {
    color: '#fff',
  },
  folderText: {
    fontSize: 14,
    fontWeight: '500',
    color: COLORS.textSecondary,
  },
  disabled: {
    opacity: 0.5,
  },
});
