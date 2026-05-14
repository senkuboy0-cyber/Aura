import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS } from '../constants/theme';

export default function EmptyState({ type = 'empty' }) {
  const content = {
    empty: {
      icon: 'document-text-outline',
      title: 'No Notes Yet',
      subtitle: 'Tap the + button to create your first note',
    },
    search: {
      icon: 'search-outline',
      title: 'No Results',
      subtitle: 'Try a different search term',
    },
    filter: {
      icon: 'filter-outline',
      title: 'No Notes Found',
      subtitle: 'No notes match the current filter',
    },
    archive: {
      icon: 'archive-outline',
      title: 'Archive Empty',
      subtitle: 'Archived notes will appear here',
    },
  };

  const { icon, title, subtitle } = content[type] || content.empty;

  return (
    <View style={styles.container}>
      <View style={styles.iconContainer}>
        <Ionicons name={icon} size={64} color={COLORS.textLight} />
      </View>
      <Text style={styles.title}>{title}</Text>
      <Text style={styles.subtitle}>{subtitle}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 60,
  },
  iconContainer: {
    width: 120,
    height: 120,
    borderRadius: 60,
    backgroundColor: COLORS.card,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 20,
  },
  title: {
    fontSize: 20,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 14,
    color: COLORS.textSecondary,
    textAlign: 'center',
    paddingHorizontal: 40,
  },
});
