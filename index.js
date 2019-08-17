import React, { PropTypes } from 'react'
import { View, requireNativeComponent } from 'react-native'

class BroadcastView extends React.Component {
  render () {
    return <RNBroadcastView {...this.props} />
  }
}

BroadcastView.propTypes = {
  /**
   * publish: If an rtmpURL is provided, will start publishing.
              If empty string provided, will stop publishing.
   */
  ...View.propTypes,
  publish: React.PropTypes.string
}

var RNBroadcastView = requireNativeComponent('RNYaseaBroadcastView', BroadcastView)

module.exports = BroadcastView
