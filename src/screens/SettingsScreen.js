import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Switch,
  Alert,
  TextInput,
  Modal,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import { useNotes } from '../context/NotesContext';
import { COLORS } from '../constants/theme';

export default function SettingsScreen() {
  const {
    notes,
    archivedNotes,
    isPinSet,
    setGlobalPinCode,
    changePin,
    removePin,
  } = useNotes();

  const [darkMode, setDarkMode] = useState(false);
  const [notifications, setNotifications] = useState(true);
  const [showPinModal, setShowPinModal] = useState(false);
  const [pinMode, setPinMode] = useState('set'); // 'set', 'change', 'remove'
  const [oldPin, setOldPin] = useState('');
  const [newPin, setNewPin] = useState('');
  const [confirmPin, setConfirmPin] = useState('');

  const stats = {
    total: notes.length,
    active: notes.filter((n) => !n.isCompleted).length,
    completed: notes.filter((n) => n.isCompleted).length,
    pinned: notes.filter((n) => n.isPinned).length,
    favorites: notes.filter((n) => n.isFavorite).length,
    archived: archivedNotes.length,
    locked: notes.filter((n) => n.isLocked).length,
  };

  const handleSetPin = async () => {
    if (newPin.length !== 4) {
      Alert.alert('Error', 'PIN must be 4 digits');
      return;
    }
    if (newPin !== confirmPin) {
      Alert.alert('Error', 'PINs do not match');
      return;
    }

    const success = await setGlobalPinCode(newPin);
    if (success) {
      Alert.alert('Success', 'Global PIN has been set');
      setShowPinModal(false);
      resetPinInputs();
    } else {
      Alert.alert('Error', 'Failed to set PIN');
    }
  };

  const handleChangePin = async () => {
    if (oldPin.length !== 4 || newPin.length !== 4) {
      Alert.alert('Error', 'PIN must be 4 digits');
      return;
    }
    if (newPin !== confirmPin) {
      Alert.alert('Error', 'New PINs do not match');
      return;
    }

    const success = await changePin(oldPin, newPin);
    if (success) {
      Alert.alert('Success', 'PIN has been changed');
      setShowPinModal(false);
      resetPinInputs();
    } else {
      Alert.alert('Error', 'Current PIN is incorrect');
    }
  };

  const handleRemovePin = async () => {
    if (oldPin.length !== 4) {
      Alert.alert('Error', 'PIN must be 4 digits');
      return;
    }

    const success = await removePin(oldPin);
    if (success) {
      Alert.alert('Success', 'PIN has been removed');
      setShowPinModal(false);
      resetPinInputs();
    } else {
      Alert.alert('Error', 'Current PIN is incorrect');
    }
  };

  const resetPinInputs = () => {
    setOldPin('');
    setNewPin('');
    setConfirmPin('');
  };

  const openPinModal = (mode) => {
    setPinMode(mode);
    setShowPinModal(true);
  };

  const handleExportData = () => {
    const data = {
      notes,
      archivedNotes,
      exportedAt: new Date().toISOString(),
    };
    Alert.alert(
      'Export Data',
      `Your data contains ${notes.length} notes. In a production app, this would be exported as a JSON file.`,
      [{ text: 'OK' }]
    );
  };

  const SettingItem = ({ icon, title, subtitle, onPress, rightComponent }) => (
    <TouchableOpacity
      style={styles.settingItem}
      onPress={onPress}
      disabled={!onPress}
    >
      <View style={styles.settingLeft}>
        <View style={styles.iconContainer}>
          <Ionicons name={icon} size={22} color={COLORS.primary} />
        </View>
        <View style={styles.settingText}>
          <Text style={styles.settingTitle}>{title}</Text>
          {subtitle && <Text style={styles.settingSubtitle}>{subtitle}</Text>}
        </View>
      </View>
      {rightComponent || (
        onPress && <Ionicons name="chevron-forward" size={20} color={COLORS.textSecondary} />
      )}
    </TouchableOpacity>
  );

  return (
    <SafeAreaView style={styles.container} edges={['top']}>
      <LinearGradient
        colors={[COLORS.primary, COLORS.primaryLight]}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={styles.header}
      >
        <Text style={styles.headerTitle}>Settings</Text>
      </LinearGradient>

      <ScrollView style={styles.scrollView} showsVerticalScrollIndicator={false}>
        {/* Statistics */}
        <View style={styles.statsContainer}>
          <Text style={styles.sectionTitle}>Your Statistics</Text>
          <View style={styles.statsGrid}>
            <View style={styles.statCard}>
              <Text style={styles.statNumber}>{stats.total}</Text>
              <Text style={styles.statLabel}>Total</Text>
            </View>
            <View style={styles.statCard}>
              <Text style={styles.statNumber}>{stats.active}</Text>
              <Text style={styles.statLabel}>Active</Text>
            </View>
            <View style={styles.statCard}>
              <Text style={styles.statNumber}>{stats.completed}</Text>
              <Text style={styles.statLabel}>Done</Text>
            </View>
            <View style={styles.statCard}>
              <Text style={styles.statNumber}>{stats.pinned}</Text>
              <Text style={styles.statLabel}>Pinned</Text>
            </View>
            <View style={styles.statCard}>
              <Text style={styles.statNumber}>{stats.favorites}</Text>
              <Text style={styles.statLabel}>Favorites</Text>
            </View>
            <View style={styles.statCard}>
              <Text style={styles.statNumber}>{stats.archived}</Text>
              <Text style={styles.statLabel}>Archived</Text>
            </View>
          </View>
        </View>

        {/* Security Section */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Security</Text>
          <View style={styles.card}>
            <SettingItem
              icon="lock-closed"
              title="Set Global PIN"
              subtitle={isPinSet ? 'PIN is set' : 'Protect your notes'}
              onPress={() => openPinModal(isPinSet ? 'change' : 'set')}
            />
            {isPinSet && (
              <>
                <View style={styles.divider} />
                <SettingItem
                  icon="key"
                  title="Change PIN"
                  subtitle="Update your security PIN"
                  onPress={() => openPinModal('change')}
                />
                <View style={styles.divider} />
                <SettingItem
                  icon="lock-open"
                  title="Remove PIN"
                  subtitle="Remove PIN protection"
                  onPress={() => openPinModal('remove')}
                />
              </>
            )}
          </View>
        </View>

        {/* Preferences Section */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Preferences</Text>
          <View style={styles.card}>
            <SettingItem
              icon="moon"
              title="Dark Mode"
              subtitle="Coming soon"
              rightComponent={
                <Switch
                  value={darkMode}
                  onValueChange={setDarkMode}
                  trackColor={{ false: COLORS.border, true: COLORS.primaryLight }}
                  thumbColor={darkMode ? COLORS.primary : COLORS.textLight}
                  disabled
                />
              }
            />
            <View style={styles.divider} />
            <SettingItem
              icon="notifications"
              title="Notifications"
              subtitle="Get reminders for due tasks"
              rightComponent={
                <Switch
                  value={notifications}
                  onValueChange={setNotifications}
                  trackColor={{ false: COLORS.border, true: COLORS.primaryLight }}
                  thumbColor={notifications ? COLORS.primary : COLORS.textLight}
                />
              }
            />
          </View>
        </View>

        {/* Data Section */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Data</Text>
          <View style={styles.card}>
            <SettingItem
              icon="download"
              title="Export Data"
              subtitle="Download your notes as JSON"
              onPress={handleExportData}
            />
            <View style={styles.divider} />
            <SettingItem
              icon="cloud-upload"
              title="Backup"
              subtitle="Coming soon"
            />
          </View>
        </View>

        {/* About Section */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>About</Text>
          <View style={styles.card}>
            <SettingItem
              icon="information-circle"
              title="App Version"
              subtitle="1.0.0"
            />
            <View style={styles.divider} />
            <SettingItem
              icon="heart"
              title="Made with React Native"
              subtitle="Expo SDK 55"
            />
          </View>
        </View>

        <View style={styles.bottomPadding} />
      </ScrollView>

      {/* PIN Modal */}
      <Modal
        visible={showPinModal}
        animationType="slide"
        transparent
        onRequestClose={() => setShowPinModal(false)}
      >
        <View style={styles.modalOverlay}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>
              {pinMode === 'set' && 'Set Global PIN'}
              {pinMode === 'change' && 'Change PIN'}
              {pinMode === 'remove' && 'Remove PIN'}
            </Text>

            {pinMode !== 'set' && (
              <View style={styles.inputContainer}>
                <Text style={styles.inputLabel}>Current PIN</Text>
                <TextInput
                  style={styles.pinInput}
                  placeholder="Enter current PIN"
                  keyboardType="number-pad"
                  maxLength={4}
                  secureTextEntry
                  value={oldPin}
                  onChangeText={setOldPin}
                />
              </View>
            )}

            {(pinMode === 'set' || pinMode === 'change') && (
              <>
                <View style={styles.inputContainer}>
                  <Text style={styles.inputLabel}>New PIN</Text>
                  <TextInput
                    style={styles.pinInput}
                    placeholder="Enter 4-digit PIN"
                    keyboardType="number-pad"
                    maxLength={4}
                    secureTextEntry
                    value={newPin}
                    onChangeText={setNewPin}
                  />
                </View>
                <View style={styles.inputContainer}>
                  <Text style={styles.inputLabel}>Confirm PIN</Text>
                  <TextInput
                    style={styles.pinInput}
                    placeholder="Confirm 4-digit PIN"
                    keyboardType="number-pad"
                    maxLength={4}
                    secureTextEntry
                    value={confirmPin}
                    onChangeText={setConfirmPin}
                  />
                </View>
              </>
            )}

            <View style={styles.modalButtons}>
              <TouchableOpacity
                style={[styles.modalButton, styles.cancelButton]}
                onPress={() => {
                  setShowPinModal(false);
                  resetPinInputs();
                }}
              >
                <Text style={styles.cancelButtonText}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.modalButton, styles.confirmButton]}
                onPress={
                  pinMode === 'set'
                    ? handleSetPin
                    : pinMode === 'change'
                    ? handleChangePin
                    : handleRemovePin
                }
              >
                <Text style={styles.confirmButtonText}>
                  {pinMode === 'remove' ? 'Remove' : 'Confirm'}
                </Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  header: {
    padding: 20,
    paddingTop: 16,
    borderBottomLeftRadius: 24,
    borderBottomRightRadius: 24,
  },
  headerTitle: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#fff',
  },
  scrollView: {
    flex: 1,
  },
  statsContainer: {
    padding: 16,
  },
  section: {
    padding: 16,
    paddingTop: 0,
  },
  sectionTitle: {
    fontSize: 14,
    fontWeight: '600',
    color: COLORS.textSecondary,
    marginBottom: 12,
    textTransform: 'uppercase',
    letterSpacing: 0.5,
  },
  statsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 12,
  },
  statCard: {
    backgroundColor: COLORS.card,
    borderRadius: 12,
    padding: 16,
    alignItems: 'center',
    width: '30%',
    shadowColor: COLORS.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  statNumber: {
    fontSize: 24,
    fontWeight: 'bold',
    color: COLORS.primary,
  },
  statLabel: {
    fontSize: 12,
    color: COLORS.textSecondary,
    marginTop: 4,
  },
  card: {
    backgroundColor: COLORS.card,
    borderRadius: 12,
    overflow: 'hidden',
    shadowColor: COLORS.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  settingItem: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 16,
  },
  settingLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  iconContainer: {
    width: 40,
    height: 40,
    borderRadius: 10,
    backgroundColor: COLORS.primaryLight + '20',
    justifyContent: 'center',
    alignItems: 'center',
  },
  settingText: {
    marginLeft: 12,
    flex: 1,
  },
  settingTitle: {
    fontSize: 16,
    fontWeight: '500',
    color: COLORS.text,
  },
  settingSubtitle: {
    fontSize: 13,
    color: COLORS.textSecondary,
    marginTop: 2,
  },
  divider: {
    height: 1,
    backgroundColor: COLORS.border,
    marginLeft: 68,
  },
  bottomPadding: {
    height: 100,
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'flex-end',
  },
  modalContent: {
    backgroundColor: COLORS.card,
    borderTopLeftRadius: 24,
    borderTopRightRadius: 24,
    padding: 24,
    paddingBottom: 40,
  },
  modalTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: COLORS.text,
    textAlign: 'center',
    marginBottom: 24,
  },
  inputContainer: {
    marginBottom: 16,
  },
  inputLabel: {
    fontSize: 14,
    color: COLORS.textSecondary,
    marginBottom: 8,
  },
  pinInput: {
    backgroundColor: COLORS.background,
    borderRadius: 12,
    padding: 16,
    fontSize: 18,
    textAlign: 'center',
    letterSpacing: 8,
    color: COLORS.text,
  },
  modalButtons: {
    flexDirection: 'row',
    gap: 12,
    marginTop: 8,
  },
  modalButton: {
    flex: 1,
    padding: 16,
    borderRadius: 12,
    alignItems: 'center',
  },
  cancelButton: {
    backgroundColor: COLORS.background,
  },
  cancelButtonText: {
    fontSize: 16,
    fontWeight: '600',
    color: COLORS.textSecondary,
  },
  confirmButton: {
    backgroundColor: COLORS.primary,
  },
  confirmButtonText: {
    fontSize: 16,
    fontWeight: '600',
    color: '#fff',
  },
});
