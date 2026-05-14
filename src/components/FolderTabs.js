import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS } from '../constants/theme';

export default function FolderTabs({ folders, selectedFolder, onSelectFolder }) {
  return (
    <View style={styles.container}>
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={styles.scrollContent}
      >
        <TouchableOpacity
          style={[
            styles.folderTab,
            selectedFolder === null && styles.activeFolderTab,
          ]}
          onPress={() => onSelectFolder(null)}
        >
          <Ionicons
            name="folder-open"
            size={18}
            color={selectedFolder === null ? '#fff' : COLORS.textSecondary}
          />
          <Text
            style={[
              styles.folderText,
              selectedFolder === null && styles.activeFolderText,
            ]}
          >
            All
          </Text>
        </TouchableOpacity>

        {folders.map((folder) => (
          <TouchableOpacity
            key={folder.id}
            style={[
              styles.folderTab,
              selectedFolder === folder.id && { backgroundColor: folder.color },
            ]}
            onPress={() => onSelectFolder(folder.id)}
          >
            <Ionicons
              name={folder.icon}
              size={18}
              color={selectedFolder === folder.id ? '#fff' : folder.color}
            />
            <Text
              style={[
                styles.folderText,
                selectedFolder === folder.id && styles.activeFolderText,
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
    marginTop: 12,
  },
  scrollContent: {
    paddingHorizontal: 16,
    gap: 8,
  },
  folderTab: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: 20,
    backgroundColor: COLORS.card,
    gap: 6,
    borderWidth: 1,
    borderColor: COLORS.border,
  },
  activeFolderTab: {
    backgroundColor: COLORS.primary,
    borderColor: COLORS.primary,
  },
  folderText: {
    fontSize: 13,
    fontWeight: '500',
    color: COLORS.textSecondary,
  },
  activeFolderText: {
    color: '#fff',
  },
});
