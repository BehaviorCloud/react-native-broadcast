import React, { PropTypes } from 'react'
import { View, requireNativeComponent } from 'react-native'

class BroadcastView extends React.Component {
  render () {
    return <RNBroadcastView {...this.props} />
  }
}

var RNBroadcastView = requireNativeComponent('RNBroadcastView', BroadcastView)

module.exports = BroadcastView
