import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  Modal,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, NOTE_COLORS } from '../constants/theme';

export default function ColorPicker({ selectedColor, onSelectColor }) {
  return (
    <View style={styles.container}>
      <Text style={styles.label}>Note Color</Text>
      <View style={styles.colorsContainer}>
        {NOTE_COLORS.map((color) => (
          <TouchableOpacity
            key={color.id}
            style={[
              styles.colorOption,
              { backgroundColor: color.color },
              selectedColor === color.id && styles.selectedColor,
            ]}
            onPress={() => onSelectColor(color.id)}
          >
            {selectedColor === color.id && (
              <Ionicons name="checkmark" size={20} color={COLORS.text} />
            )}
          </TouchableOpacity>
        ))}
      </View>
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
  colorsContainer: {
    flexDirection: 'row',
    gap: 12,
  },
  colorOption: {
    width: 48,
    height: 48,
    borderRadius: 24,
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 2,
    borderColor: 'transparent',
  },
  selectedColor: {
    borderColor: COLORS.primary,
    shadowColor: COLORS.primary,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.3,
    shadowRadius: 4,
    elevation: 4,
  },
});
