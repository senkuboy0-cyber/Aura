import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, FILTERS } from '../constants/theme';

const FILTERS_DATA = [
  { id: FILTERS.ALL, label: 'All', icon: 'list' },
  { id: FILTERS.ACTIVE, label: 'Active', icon: 'ellipse-outline' },
  { id: FILTERS.COMPLETED, label: 'Done', icon: 'checkmark-circle-outline' },
  { id: FILTERS.PINNED, label: 'Pinned', icon: 'pin' },
  { id: FILTERS.FAVORITES, label: 'Favorites', icon: 'star-outline' },
];

export default function FilterTabs({ activeFilter, onFilterChange }) {
  return (
    <View style={styles.container}>
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={styles.scrollContent}
      >
        {FILTERS_DATA.map((filter) => (
          <TouchableOpacity
            key={filter.id}
            style={[
              styles.tab,
              activeFilter === filter.id && styles.activeTab,
            ]}
            onPress={() => onFilterChange(filter.id)}
          >
            <Ionicons
              name={filter.icon}
              size={16}
              color={activeFilter === filter.id ? '#fff' : COLORS.textSecondary}
            />
            <Text
              style={[
                styles.tabText,
                activeFilter === filter.id && styles.activeTabText,
              ]}
            >
              {filter.label}
            </Text>
          </TouchableOpacity>
        ))}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginTop: 16,
  },
  scrollContent: {
    paddingHorizontal: 16,
    gap: 8,
  },
  tab: {
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
  activeTab: {
    backgroundColor: COLORS.primary,
    borderColor: COLORS.primary,
  },
  tabText: {
    fontSize: 13,
    fontWeight: '500',
    color: COLORS.textSecondary,
  },
  activeTabText: {
    color: '#fff',
  },
});
