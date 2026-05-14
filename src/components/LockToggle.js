import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  TextInput,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS } from '../constants/theme';

export default function LockToggle({
  isLocked,
  onToggle,
  pin,
  onPinChange,
  showPinInput,
  onPinSubmit,
  unlockMode,
}) {
  return (
    <View style={styles.container}>
      <Text style={styles.label}>Lock Note</Text>
      
      <View style={styles.toggleContainer}>
        <View style={styles.toggleInfo}>
          <Ionicons
            name={isLocked ? 'lock-closed' : 'lock-open-outline'}
            size={24}
            color={isLocked ? COLORS.warning : COLORS.textSecondary}
          />
          <View style={styles.toggleText}>
            <Text style={styles.toggleTitle}>
              {unlockMode ? 'Unlock Note' : 'Password Protection'}
            </Text>
            <Text style={styles.toggleSubtitle}>
              {isLocked
                ? 'This note is protected with a PIN'
                : 'Add a 4-digit PIN to lock this note'}
            </Text>
          </View>
        </View>

        {!unlockMode && (
          <TouchableOpacity
            style={[
              styles.toggleButton,
              isLocked && styles.toggleButtonActive,
            ]}
            onPress={() => onToggle(!isLocked)}
          >
            <View
              style={[
                styles.toggleTrack,
                isLocked && styles.toggleTrackActive,
              ]}
            >
              <View
                style={[
                  styles.toggleThumb,
                  isLocked && styles.toggleThumbActive,
                ]}
              />
            </View>
          </TouchableOpacity>
        )}
      </View>

      {/* PIN Input for unlocking */}
      {unlockMode && showPinInput && (
        <View style={styles.pinInputContainer}>
          <Text style={styles.pinLabel}>Enter PIN to unlock</Text>
          <View style={styles.pinInputRow}>
            <TextInput
              style={styles.pinInput}
              placeholder="****"
              placeholderTextColor={COLORS.textLight}
              keyboardType="number-pad"
              maxLength={4}
              secureTextEntry
              value={pin}
              onChangeText={onPinChange}
              autoFocus
            />
            <TouchableOpacity
              style={[styles.unlockButton, pin.length !== 4 && styles.unlockButtonDisabled]}
              onPress={onPinSubmit}
              disabled={pin.length !== 4}
            >
              <Ionicons name="checkmark" size={24} color="#fff" />
            </TouchableOpacity>
          </View>
        </View>
      )}

      {/* PIN Input for setting lock */}
      {isLocked && !unlockMode && (
        <View style={styles.pinInputContainer}>
          <Text style={styles.pinLabel}>Enter 4-digit PIN</Text>
          <View style={styles.pinInputRow}>
            <TextInput
              style={styles.pinInput}
              placeholder="****"
              placeholderTextColor={COLORS.textLight}
              keyboardType="number-pad"
              maxLength={4}
              secureTextEntry
              value={pin}
              onChangeText={onPinChange}
            />
          </View>
          <Text style={styles.pinHint}>
            <Ionicons name="information-circle-outline" size={14} color={COLORS.textSecondary} />
            {' '}You'll need this PIN to unlock the note
          </Text>
        </View>
      )}
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
  toggleContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: COLORS.card,
    padding: 16,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: COLORS.border,
  },
  toggleInfo: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
    gap: 12,
  },
  toggleText: {
    flex: 1,
  },
  toggleTitle: {
    fontSize: 16,
    fontWeight: '500',
    color: COLORS.text,
  },
  toggleSubtitle: {
    fontSize: 13,
    color: COLORS.textSecondary,
    marginTop: 2,
  },
  toggleButton: {
    padding: 4,
  },
  toggleButtonActive: {},
  toggleTrack: {
    width: 52,
    height: 32,
    borderRadius: 16,
    backgroundColor: COLORS.border,
    padding: 2,
    justifyContent: 'center',
  },
  toggleTrackActive: {
    backgroundColor: COLORS.warning + '40',
  },
  toggleThumb: {
    width: 28,
    height: 28,
    borderRadius: 14,
    backgroundColor: '#fff',
    shadowColor: COLORS.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
    shadowRadius: 2,
    elevation: 2,
  },
  toggleThumbActive: {
    transform: [{ translateX: 20 }],
  },
  pinInputContainer: {
    marginTop: 16,
    backgroundColor: COLORS.card,
    padding: 16,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: COLORS.border,
  },
  pinLabel: {
    fontSize: 14,
    fontWeight: '500',
    color: COLORS.text,
    marginBottom: 12,
  },
  pinInputRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
  },
  pinInput: {
    flex: 1,
    backgroundColor: COLORS.background,
    borderRadius: 12,
    padding: 16,
    fontSize: 24,
    textAlign: 'center',
    letterSpacing: 8,
    color: COLORS.text,
    fontWeight: 'bold',
  },
  unlockButton: {
    width: 56,
    height: 56,
    borderRadius: 12,
    backgroundColor: COLORS.success,
    justifyContent: 'center',
    alignItems: 'center',
  },
  unlockButtonDisabled: {
    backgroundColor: COLORS.border,
  },
  pinHint: {
    fontSize: 12,
    color: COLORS.textSecondary,
    marginTop: 12,
  },
});
